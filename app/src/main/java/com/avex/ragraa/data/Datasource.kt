package com.avex.ragraa.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.avex.ragraa.data.dataclasses.Attendance
import com.avex.ragraa.data.dataclasses.Course
import com.avex.ragraa.data.dataclasses.CourseAttendance
import com.avex.ragraa.data.dataclasses.CourseMarks
import com.avex.ragraa.data.dataclasses.Marks
import com.avex.ragraa.data.dataclasses.Section
import com.avex.ragraa.data.dataclasses.Semester
import com.avex.ragraa.data.dataclasses.Transcript
import com.avex.ragraa.data.dataclasses.TranscriptCourse
import com.avex.ragraa.data.dataclasses.attendanceHTML
import com.avex.ragraa.data.dataclasses.imageByteArray
import com.avex.ragraa.data.dataclasses.marksHTML
import com.avex.ragraa.data.dataclasses.transcriptHTML
import com.avex.ragraa.sharedPreferences
import com.avex.ragraa.store
import com.avex.ragraa.ui.Screens
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

    var captchaKey: String = ""

    var overrideSystemTheme: Boolean = false
    var darkTheme: Boolean = true

    lateinit var initScreen: Screens

    var bitmap: ImageBitmap? = null

    var courses: MutableList<Course> = mutableListOf()

    // tracks new additions for background notif system
    var newAdditions: MutableSet<Triple<String, String, String>> = mutableSetOf()

    private var marksDatabase: MutableList<CourseMarks> = mutableListOf()
    private var attendanceDatabase: MutableList<CourseAttendance> = mutableListOf()

    var transcriptDatabase: Transcript = Transcript(0f, listOf())

    var semId: String = ""
    var male: Boolean = true
    var niqab: Boolean = false
    var firstTime = true

    var updateLoginUI: () -> Unit = {}
    var updateHomeUI: () -> Unit = {}
    var initCalculator: () -> Unit = {}
    var updateTranscriptUI: () -> Unit = {}

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

        initScreen = if (rollNo.isEmpty()) Screens.Login else Screens.Home

        password = sharedPreferences.getString("password", "").toString()
        if (password.isNotEmpty()) {
            password = password.decrypt()
        }

        overrideSystemTheme = sharedPreferences.getBoolean("overrideSystemTheme", false)

        darkTheme = sharedPreferences.getBoolean("darkTheme", true)

        date = sharedPreferences.getString("date", "").toString()

        showImage = sharedPreferences.getBoolean("showImage", false)

        male = sharedPreferences.getBoolean("male", true)

        firstTime = sharedPreferences.getBoolean("firstTime", true)

        semId = sharedPreferences.getString("semId", "251").toString()

        niqab = sharedPreferences.getBoolean("niqab", false)

        captchaKey = sharedPreferences.getString("captchaKey", "").toString()

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

    fun setOverride(bool: Boolean) {
        overrideSystemTheme = bool
        sharedPreferences.edit().putBoolean("overrideSystemTheme", bool).apply()
    }

    fun setDark(bool: Boolean) {
        darkTheme = bool
        sharedPreferences.edit().putBoolean("darkTheme", bool).apply()
    }

    fun setGender(isMale: Boolean) {
        male = isMale
        showImage = isMale
        sharedPreferences.edit().putBoolean("firstTime", false).apply()
        sharedPreferences.edit().putBoolean("male", male).apply()
        firstTime = false
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

    var marksParsed = false
    var attendanceParsed = false


    private fun combineMarksAndAttendance() {
        if (!marksParsed || !attendanceParsed) return

        val attendancesName =
            attendanceDatabase.map { it.courseName }
        val marksName = marksDatabase.map { it.courseName }

        val newCourses: MutableList<Course> = mutableListOf()

        for (marks in marksName) {
            val indexMarks = marksName.indexOf(marks)
            val indexAttendance = attendancesName.indexOf(marks)

            var listAttendance: List<Attendance> = listOf()
            var percentage = 100f
            var absents = 0

            if (indexAttendance != -1) {
                listAttendance = attendanceDatabase[indexAttendance].attendance
                percentage = attendanceDatabase[indexAttendance].percentage
                absents = attendanceDatabase[indexAttendance].absents
            }

            newCourses.add(
                Course(
                    name = marksDatabase[indexMarks].courseName,
                    marks = marksDatabase[indexMarks].courseMarks,
                    newMarks = marksDatabase[indexMarks].new,
                    grandTotalExists = marksDatabase[indexMarks].grandTotalExists,
                    attendance = listAttendance,
                    attendancePercentage = percentage,
                    attendanceAbsents = absents
                )
            )
        }

        for (attendance in attendancesName) {
            val indexAttendance = attendancesName.indexOf(attendance)
            val indexMarks = marksName.indexOf(attendance)
            if (indexMarks == -1)
                newCourses.add(
                    Course(
                        name = attendance,
                        marks = listOf(),
                        newMarks = false,
                        grandTotalExists = false,
                        attendance = attendanceDatabase[indexAttendance].attendance,
                        attendancePercentage = attendanceDatabase[indexAttendance].percentage,
                        attendanceAbsents = attendanceDatabase[indexAttendance].absents
                    )
                )
        }


        courses = newCourses
        updateHomeUI()
        updateHomeUI()
    }

    fun parseMarks() {
        if (marksResponse.isEmpty()) {
            Log.d("Dev", "Flex data has not been fetched yet")
        }

        val htmlFile = Jsoup.parse(marksResponse).body()

        val newDatabase: MutableList<CourseMarks> = mutableListOf()

        //Divide into courses
        val totalCourses = htmlFile.getElementsByAttributeValue("id", "accordion")

        for (course in totalCourses) {
            val listOfItems: MutableList<Section> = mutableListOf()
            var totalWeightage = 0f

            var projectedObt = 0f
            var projectedTotal = 0f
            var projectedAvg = 0f
            var grandTotalExists = false

            val mb0 = course.getElementsByClass("mb-0")
            val tables = course.getElementsByClass("sum_table table m-table m-table--head-bg-info table-bordered table-striped table-responsive")
            val obtMarksElements = course.getElementsByClass("text-center totalColObtMarks")
            val weightageElements = course.getElementsByClass("text-center totalColweightage")

            for ((i, courseWork) in mb0.withIndex()) {
                if (i == mb0.size - 1) {
                    if (tables.getOrNull(i)?.getElementsByClass("GrandtotalColumn")?.isNotEmpty() == true) grandTotalExists = true
                    continue
                }

                val currentTable = tables.getOrNull(i) ?: continue

                val listOfMarks: MutableList<Marks> = mutableListOf()
                var number = 1

                for (courseWorkMerit in currentTable.getElementsByClass("calculationrow")) {
                    val temp: MutableList<Float> = mutableListOf()

                    for ((v, individualItem) in courseWorkMerit.getElementsByClass("text-center")
                        .withIndex()) {
                        if (v == 0) continue

                        val thing: Float = when (val text = individualItem.text()) {
                            "", "-" -> -1f
                            else -> text.toFloatOrNull() ?: -1f
                        }

                        temp.add(thing)
                    }

                    if (temp.isNotEmpty()) {
                        totalWeightage += temp[0]

                        listOfMarks.add(
                            Marks(
                                number,
                                temp[0],
                                temp.getOrNull(1) ?: 0f,
                                temp.getOrNull(2) ?: 0f,
                                temp.getOrNull(3) ?: 0f,
                                temp.getOrNull(5) ?: 0f,
                                temp.getOrNull(6) ?: 0f,
                            )
                        )
                    }

                    number++
                }

                var average = 0f

                for (item in listOfMarks) {
                    if (!(item.average.isNaN() || item.total.isNaN() || item.weightage.isNaN() || item.obtained.isNaN() || item.average.toInt() == -1))
                        average += item.average / item.total * item.weightage
                }

                val obtained = obtMarksElements.getOrNull(i)?.text()?.toFloatOrNull() ?: 0f
                val total = weightageElements.getOrNull(i)?.text()?.toFloatOrNull() ?: 0f

                projectedAvg += if (average.isNaN()) 0f else average
                projectedTotal += total
                projectedObt += obtained


                listOfItems.add(
                    Section(
                        courseWork.text(), listOfMarks, obtained, total, average
                    )
                )
            }

            if (grandTotalExists)
                listOfItems.add(
                    Section(
                        "Grand Total",
                        listOf(),
                        projectedObt,
                        projectedTotal,
                        projectedAvg
                    )
                )
            else listOfItems.add(
                Section(
                    "Projected Total",
                    listOf(),
                    projectedObt,
                    projectedTotal,
                    projectedAvg
                )
            )

            newDatabase.add(
                CourseMarks(
                    course.getElementsByTag("h5").firstOrNull()?.text() ?: "Unknown",
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

        marksParsed = true
        combineMarksAndAttendance()
    }

    //This is the greatest piece of code i've ever written
    private fun checkAdditions(newDatabase: MutableList<CourseMarks>) {
        if (marksDatabase.isEmpty()) {
            return
        }

        val databaseCourseNames = marksDatabase.map { it.courseName }
        newAdditions = mutableSetOf() // CourseName, Section, Marks


        for (course in newDatabase) {
            val index: Int = databaseCourseNames.indexOf(course.courseName)

            if (index == -1) {
                course.new = true
                for (section in course.courseMarks) {
                    section.new = true
                    for (marks in section.listOfMarks) marks.new = true
                }
                newAdditions.add(Triple(course.courseName, "ALL", ""))
                continue
            }

            val databaseCourseMarks = marksDatabase[index].courseMarks.map { it.name }
            for (section in course.courseMarks) {
                val index2: Int = databaseCourseMarks.indexOf(section.name)

                if (index2 == -1) {
                    course.new = true
                    section.new = true
                    newAdditions.add(Triple(course.courseName, section.name, "${section.obtained}/${section.total}"))
                    for (marks in section.listOfMarks) {
                        marks.new = true
                    }
                    continue
                }

                val originalMarks =
                    marksDatabase[index].courseMarks[index2].listOfMarks.map { it.copy(weightage = 0f, average = 0f, minimum = 0f, maximum = 0f) }

                for (marks in section.listOfMarks) {
                    val newMarks = marks.copy(weightage = 0f, average = 0f, minimum = 0f, maximum = 0f)
                    if (!originalMarks.contains(newMarks)) {
                        newAdditions.add(Triple(course.courseName, section.name, "${marks.obtained}/${marks.total}"))
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
    }

    fun parseAttendance() {
        attendanceDatabase.clear()

        val htmlFile = Jsoup.parse(attendanceResponse).body()

        val courses = htmlFile.getElementsByAttributeValue("role", "tabpanel")

        for (course in courses) {
            val courseName = course.getElementsByClass("col-md-6").firstOrNull()?.text() ?: "Unknown"

            val intermediateObject =
                if (course.getElementsByClass("progress-bar progress-bar-striped progress-bar-animated bg-success")
                        .isNotEmpty()
                )
                    course.getElementsByClass("progress-bar progress-bar-striped progress-bar-animated bg-success")[0]
                else
                    course.getElementsByClass("progress-bar progress-bar-striped progress-bar-animated bg-danger").firstOrNull()

            if (intermediateObject == null || intermediateObject.text().isEmpty())
                continue

            val percentageStr = intermediateObject.text()
            val percentage = if (percentageStr.length >= 2) {
                percentageStr.substring(0, percentageStr.length - 2).toFloatOrNull() ?: 0f
            } else 0f

            val listAttendance: MutableList<Attendance> = mutableListOf()

            val table = course.getElementsByClass("table table-bordered table-responsive m-table m-table--border-info m-table--head-bg-info").firstOrNull()
            val courseDetails = table?.getElementsByClass("text-center") ?: listOf()

            var date: String? = null
            var presence: Char? = null
            var absent = 0

            for (textCenter in courseDetails) {
                val text = textCenter.text()
                if (text.contains('-')) date = text

                if (text.length > 1)
                    continue

                if (text.isNotEmpty() && (text[0] == 'P' || text[0] == 'A' || text[0] == 'L')) {
                    presence = text[0]
                }
                if (text.isNotEmpty() && text[0] == 'A') absent++

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

        attendanceParsed = true
        combineMarksAndAttendance()
    }

    fun parseTranscript() {
        val htmlFile = Jsoup.parse(transcriptResponse).body()

        val semesterList: MutableList<Semester> = mutableListOf()
        var cgpa = 0f

        val semesters = htmlFile.getElementsByClass("col-md-6")

        for (semester in semesters) {
            val courseList: MutableList<TranscriptCourse> = mutableListOf()
            val semesterName = semester.select("h5").text().trim()

            for (row in semester.select("tbody tr")) {
                val tds = row.select("td")
                if (tds.size < 6) continue

                val grade = tds[4].text().trim()
                val isRelative = tds[0].select("a").isNotEmpty() || tds[0].attr("style").contains("underline")

                courseList.add(
                    TranscriptCourse(
                        courseID = tds[0].text().trim(),
                        courseName = tds[1].text().trim(),
                        creditHours = tds[3].text().trim().toIntOrNull() ?: 0,
                        grade = if (grade == "I") "" else grade,
                        gpa = tds[5].text().trim().toFloatOrNull() ?: 0f,
                        isRelative = isRelative
                    )
                )
            }

            var sgpa = 0f
            val pullRight = semester.select(".pull-right").firstOrNull()
            if (pullRight != null) {
                val text = pullRight.text().replace(" ", "")

                if (text.contains("CGPA:")) {
                    val valStr = text.substringAfter("CGPA:").takeWhile { it.isDigit() || it == '.' }
                    cgpa = valStr.toFloatOrNull() ?: cgpa
                }
                if (text.contains("SGPA:")) {
                    val valStr = text.substringAfter("SGPA:").takeWhile { it.isDigit() || it == '.' }
                    sgpa = valStr.toFloatOrNull() ?: 0f
                }
            }

            if (courseList.isNotEmpty() || semesterName.isNotEmpty()) {
                semesterList.add(Semester(sgpa, semesterName, courseList))
            }
        }

        transcriptDatabase = Transcript(cgpa, semesterList)

        initCalculator()
        updateTranscriptUI()

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