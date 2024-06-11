package com.avex.ragraa.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.avex.ragraa.sharedPreferences
import com.avex.ragraa.store
import io.objectbox.kotlin.boxFor
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Datasource {
    var marksResponse = ""
    var attendanceResponse = ""
    var transcriptResponse = ""

    var rollNo: String = ""
    var password: String = ""
    var showImage: Boolean = true
    var date: String = ""

    var bitmap: ImageBitmap? = null
    var marksDatabase: MutableList<Course> = mutableListOf()
    var attendanceDatabase: MutableList<CourseAttendance> = mutableListOf()
    var transcriptDatabase: Transcript? = null

    var semId: String = ""

    var updateLoginUI: () -> Unit = {}
    var updateHomeUI: () -> Unit = {}

    fun cacheData() {
        val attendanceBox = store.boxFor<attendanceHTML>().all
        if (attendanceBox.isNotEmpty()) {
            attendanceResponse = attendanceBox[0].html
            parseAttendance()
        } else Log.d("Dev", "Attendance is empty")

        val marksBox = store.boxFor<marksHTML>().all
        if (marksBox.isNotEmpty()) {
            marksResponse = marksBox[0].html
            parseMarks()
        } else Log.d("Dev", "Marks is empty")

        val transcriptBox = store.boxFor<transcriptHTML>().all
        if (transcriptBox.isNotEmpty()) {
            transcriptResponse = transcriptBox[0].html
            parseTranscript()
        } else Log.d("Dev", "Transcript is empty")

        rollNo = sharedPreferences.getString("rollNo", "").toString()
        if (rollNo.isNotEmpty()) {
            rollNo = rollNo.decrypt()
        }

        password = sharedPreferences.getString("password", "").toString()
        if (password.isNotEmpty()) {
            password = password.decrypt()
        }

        date = sharedPreferences.getString("date", "").toString()
        Log.d("Dev", date)
        showImage = sharedPreferences.getBoolean("showImage", true)

        semId = sharedPreferences.getString("semId", "241").toString()

        if (rollNo.isEmpty()) return

        val bitmapBox = store.boxFor<imageByteArray>().all

        if (bitmapBox.isNotEmpty()) {
            bitmap = BitmapFactory.decodeByteArray(
                bitmapBox[0].byteArray, 0, bitmapBox[0].byteArray.size
            ).asImageBitmap()
        } else {
            bitmap = null; Log.d("Dev", "Query is empty")
        }

        updateHomeUI()
    }

    fun saveLogin() {
        val editor = sharedPreferences.edit()
        editor.putString("rollNo", rollNo.encrypt())
        editor.putString("password", password.encrypt())
        editor.putString(
            "date", SimpleDateFormat(
                "dd MMM, HH:mm", Locale.getDefault()
            ).format(Calendar.getInstance().time)
        )
        editor.apply()
    }

    fun parseMarks() {
        if (marksResponse.isEmpty()) {
            Log.d("Dev", "Flex data has not been fetched yet")
        }

        val htmlFile = Jsoup.parse(marksResponse).body()

        val newDatabase: MutableList<Course> = mutableListOf()

        //Divide into courses
        val totalCourses = htmlFile.getElementsByAttributeValue("id", "accordion")

        for (course in totalCourses) {

            val listOfItems: MutableList<Section> = mutableListOf()
            var totalWeightage = 0f

            var projectedObt = 0f
            var projectedTotal = 0f
            var projectedAvg = 0f
            var grandTotalExists = false

            for ((i, courseWork) in course.getElementsByClass("mb-0").withIndex()) {
                if (i == course.getElementsByClass("mb-0").size - 1) {
                    if (course.getElementsByClass("sum_table table m-table m-table--head-bg-info table-bordered table-striped table-responsive")[i].getElementsByClass(
                            "GrandtotalColumn"
                        ).isNotEmpty()
                    ) grandTotalExists = true

                    continue
                }

                val listOfMarks: MutableList<Marks> = mutableListOf()

                for (courseWorkMerit in course.getElementsByClass("sum_table table m-table m-table--head-bg-info table-bordered table-striped table-responsive")[i].getElementsByClass(
                    "calculationrow"
                )) {
                    val temp: MutableList<Float> = mutableListOf()

                    for ((v, individualItem) in courseWorkMerit.getElementsByClass("text-center")
                        .withIndex()) {
                        if (v == 0) continue

                        val thing: Float = when (individualItem.text()) {
                            "", "-" -> (-1).toFloat()
                            else -> individualItem.text().toFloat()
                        }

                        temp.add(thing)
                    }

                    totalWeightage += temp[0]

                    listOfMarks.add(
                        Marks(
                            temp[0],
                            temp[1],
                            temp[2],
                            temp[3],
                            temp[5],
                            temp[6],
                        )
                    )
                }


                var average = 0f

                for (item in listOfMarks) {
                    average += item.average / item.total * item.weightage
                }
                val obtained =
                    course.getElementsByClass("text-center totalColObtMarks")[i].text().toFloat()
                val total =
                    course.getElementsByClass("text-center totalColweightage")[i].text().toFloat()

                projectedAvg += average
                projectedTotal += total
                projectedObt += obtained

                listOfItems.add(
                    Section(
                        courseWork.text(), listOfMarks, obtained, total, average
                    )
                )

            }


            listOfItems.add(
                Section(
                    if (grandTotalExists) "Grand Total" else "Projected Total",
                    listOf(),
                    projectedObt,
                    projectedTotal,
                    projectedAvg
                )
            )

            newDatabase.add(
                Course(
                    course.getElementsByTag("h5")[0].text(),
                    listOfItems,
                    grandTotalExists = grandTotalExists
                )
            )

        }

        checkAdditions(newDatabase)
        marksDatabase = newDatabase

        val box = store.boxFor<marksHTML>()
        box.removeAll()
        box.put(marksHTML(marksResponse))

        updateHomeUI()
    }

    //This is the greatest piece of code i've ever written
    private fun checkAdditions(newDatabase: MutableList<Course>) {
        if (marksDatabase.isEmpty()) {
            return
        }

        val databaseCourseNames = marksDatabase.map { it.courseName }

        for (course in newDatabase) {
            val index: Int = databaseCourseNames.indexOf(course.courseName)

            if (index == -1) {
                course.new = true
                for (section in course.courseMarks) {
                    section.new = true
                    for (marks in section.listOfMarks) marks.new = true
                }
                continue
            }

            val databaseCourseMarks = marksDatabase[index].courseMarks.map { it.name }
            for (section in course.courseMarks) {
                val index2: Int = databaseCourseMarks.indexOf(section.name)

                if (index2 == -1) {
                    course.new = true
                    section.new = true
                    for (marks in section.listOfMarks) {
                        marks.new = true
                    }
                    continue
                }

                val originalMarks =
                    marksDatabase[index].courseMarks[index2].listOfMarks.map { it.copy(weightage = 0f) }

                for (marks in section.listOfMarks) {
                    val newMarks = marks.copy(weightage = 0f)
                    if (!originalMarks.contains(newMarks)) {
                        course.new = true
                        section.new = true
                        marks.new = true
                    }
                }
            }
        }
    }

    fun saveImage(response: Response, rollNo: String) {
        val box = store.boxFor<imageByteArray>()
        box.removeAll()
        val inputStream = response.body?.byteStream()
        bitmap = BitmapFactory.decodeStream(inputStream).asImageBitmap()
        val bos = ByteArrayOutputStream()
        bitmap?.asAndroidBitmap()?.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bArray = bos.toByteArray()

        updateHomeUI()

        box.put(imageByteArray(bArray, rollNo, 0))
        Log.d("Dev", "Creating user")
    }

    fun parseAttendance() {
        val htmlFile = Jsoup.parse(attendanceResponse).body()

        val courses = htmlFile.getElementsByAttributeValue("role", "tabpanel")

        for (course in courses) {
            val courseName = course.getElementsByClass("col-md-6")[0].text()
            val percentage =
                course.getElementsByClass("progress-bar progress-bar-striped progress-bar-animated bg-success")[0].text()
                    .substring(
                        0,
                        course.getElementsByClass("progress-bar progress-bar-striped progress-bar-animated bg-success")[0].text().length - 2
                    ).toFloat()
            val listAttendance: MutableList<Attendance> = mutableListOf()

            val courseDetails =
                course.getElementsByClass("table table-bordered table-responsive m-table m-table--border-info m-table--head-bg-info")[0].getElementsByClass(
                    "text-center"
                )

            var date: String? = null
            var presence: Char? = null
            var absent = 0

            for (textCenter in courseDetails) {
                if (textCenter.text().contains('-')) date = textCenter.text()
                if (textCenter.text()[0] == 'P' || textCenter.text()[0] == 'A' || textCenter.text()[0] == 'L') {
                    presence = textCenter.text()[0]
                }
                if (textCenter.text()[0] == 'A') absent++

                if (date != null && presence != null) {
                    listAttendance.add(Attendance(date, presence))
                    date = null
                    presence = null
                }
            }
            attendanceDatabase.add(CourseAttendance(courseName, percentage, listAttendance, absent))
        }

        val box = store.boxFor<attendanceHTML>()
        box.removeAll()
        box.put(attendanceHTML(attendanceResponse))
    }

    fun parseTranscript() {
        val htmlFile = Jsoup.parse(transcriptResponse).body()

        val semesterList: MutableList<Semester> = mutableListOf()
        var sgpa = 0f

        for (semester in htmlFile.getElementsByClass("col-md-6")) {
            val courseList: MutableList<TranscriptCourse> = mutableListOf()


            val semesterName = semester.getElementsByTag("h5").text()

            for (course in semester.getElementsByTag("tr")) {
                if (course.getElementsByTag("th").isNotEmpty()) continue

                val grade = course.getElementsByTag("td")[4].text()

                val tempCourse = TranscriptCourse(
                    courseID = course.getElementsByTag("td")[0].text(),
                    courseName = course.getElementsByTag("td")[1].text(),
                    creditHours = course.getElementsByTag("td")[3].text().toInt(),
                    grade = if (grade == "I") "" else grade,
                    gpa = course.getElementsByTag("td")[5].text().toFloat()
                )

                courseList.add(tempCourse)
            }

            val dataList = semester.getElementsByClass("pull-right")[0].html()

            val cData = dataList.substring(dataList.indexOf("CGPA"))
            val cgpa = cData.substring(5, cData.indexOf('<')).toFloat()

            val sData = dataList.substring(dataList.indexOf("SGPA"))
            sgpa = sData.substring(5, sData.indexOf('<')).toFloat()


            semesterList.add(Semester(cgpa, semesterName, courseList))
        }

        transcriptDatabase = Transcript(sgpa, semesterList)

        val box = store.boxFor<transcriptHTML>()
        box.removeAll()
        box.put(transcriptHTML(transcriptResponse))
    }
}

fun String.encrypt(password: String = "tK5UTui+DPh8lIlB"): String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val iv = ByteArray(16)
    val charArray = password.toCharArray()
    for (i in 0 until 16) {
        iv[i] = charArray[i].code.toByte()
    }

    val ivParameterSpec = IvParameterSpec(iv)

    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

    val encryptedValue = cipher.doFinal(this.toByteArray())
    return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
}

fun String.decrypt(password: String = "tK5UTui+DPh8lIlB"): String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val iv = ByteArray(16)
    val charArray = password.toCharArray()
    for (i in 0 until 16) {
        iv[i] = charArray[i].code.toByte()
    }
    val ivParameterSpec = IvParameterSpec(iv)

    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

    val decryptedByteValue = cipher.doFinal(Base64.decode(this, Base64.DEFAULT))
    return String(decryptedByteValue)
}