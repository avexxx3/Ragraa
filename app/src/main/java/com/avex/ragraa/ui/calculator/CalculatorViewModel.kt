package com.avex.ragraa.ui.calculator

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.Datasource.transcriptDatabase
import com.avex.ragraa.data.dataclasses.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.round
import kotlin.math.roundToInt

class CalculatorViewModel : ViewModel() {
    lateinit var navController: NavHostController

    private val _uiState = MutableStateFlow(CalculatorUiState(mutableListOf()))
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val courses: MutableList<CalculatorCourse> = mutableListOf()
    private var index: Int = 0
    private var editingCourse: CalculatorCourse? = null
    private var totalGPA: Float = 0f

    var currentCourse: Course? = null
    private var viewingCourseMarks = false

    fun init() {
        Datasource.initCalculator = { updateUI() }

        val transcript =
            if (transcriptDatabase.semesters.isNotEmpty()) transcriptDatabase.semesters.last().courses.map { it.courseID } else listOf()

        val marks = Datasource.courses.map { it.name }

        courses.clear()

        for (course in marks) {

            var locked = false

            val index = transcript.indexOf(course.substring(0..5))

            val transcriptCourse =
                if (index != -1) transcriptDatabase.semesters.last().courses[index] else null

            val marksCourse = Datasource.courses[marks.indexOf(course)]

            if (index != -1) locked = transcriptCourse?.grade!!.isNotEmpty()


            // If the course exists in the transcript and it has a grade uploaded,
            // then it'll be preferred over the data from the marks
            // Otherwise data is filled from the marks uploaded on Flex

            if (index == -1) {
                courses.add(
                    CalculatorCourse(
                        name = marksCourse.name.substring(7),
                        credits = "0",
                        mca = Datasource.courses[marks.indexOf(course)].marks.last().average.roundToInt()
                            .toString(),
                        obtained = Datasource.courses[marks.indexOf(course)].marks.last().obtained.roundToInt()
                            .toString(),
                        gpa = 0f,
                        grade = "",
                        isRelative = false,
                        locked = false
                    )
                )
            } else courses.add(
                CalculatorCourse(
                    name = marksCourse.name.substring(7),
                    credits = transcriptCourse?.creditHours.toString(),
                    mca = Datasource.courses[marks.indexOf(course)].marks.last().average.roundToInt()
                        .toString(),
                    obtained = Datasource.courses[marks.indexOf(course)].marks.last().obtained.roundToInt()
                        .toString(),
                    gpa = if (locked) transcriptCourse!!.gpa else 0f,
                    grade = if (locked) transcriptCourse!!.grade else "",
                    isRelative = transcriptCourse!!.isRelative,
                    locked = locked
                )
            )
        }

        calculateGPA()
        updateUI()
    }

    init {
        init()
    }

    private fun updateUI() {
        _uiState.update {
            it.copy(
                courses = courses,
                editingCourse = editingCourse,
                viewingCourseMarks = viewingCourseMarks,
                overallGpa = totalGPA
            )
        }
    }

    fun editCourse(course: CalculatorCourse? = null) {
        editingCourse = course
        index = courses.indexOf(course)
        currentCourse =
            if (index >= 0) if (!course!!.isCustom) Datasource.courses[index] else null else null
        updateUI()
    }

    fun viewMarks() {
        viewingCourseMarks = !viewingCourseMarks
        updateUI()
    }

    fun updateMca(it: String) {
        if (!validString(it)) return
        editingCourse = editingCourse?.copy(mca = it)
        updateUI()
    }

    fun updateCredits(it: String) {
        if (it.isDigitsOnly() && it.length <= 1) editingCourse = editingCourse?.copy(credits = it)
        updateUI()
    }

    fun updateObtained(it: String) {
        if (!validString(it)) return
        editingCourse = editingCourse?.copy(obtained = it)
        updateUI()
    }

    fun updateName(it: String) {
        editingCourse = editingCourse?.copy(name = it)
        updateUI()
    }

    fun toggleRelative() {
        editingCourse = editingCourse?.copy(isRelative = !editingCourse?.isRelative!!)
        updateUI()
    }

    fun saveCourse() {
        calculateGrade()
        courses[index] = editingCourse!!
        calculateGPA()
        editingCourse = null
        updateUI()
    }

    fun addCourse() {
        index = courses.size
        courses.add(CalculatorCourse(isCustom = true))
        editingCourse = courses.last()
        updateUI()
    }

    private fun validString(it: String): Boolean {
        var counter = 0
        it.forEach { char ->
            if (char == '.') counter++
        }

        return !((it.toFloatOrNull() == null || counter > 1) && it.isNotEmpty())
    }

    private fun calculateGPA() {
        var totalCredits = 0
        totalGPA = 0f

        for (course in courses) {
            if (course.credits.isNotEmpty() && course.grade.isNotEmpty() && course.grade != "S") {
                totalCredits += course.credits.toInt()
            }
        }

        for (course in courses) {
            if (course.credits.isNotEmpty() && course.grade.isNotEmpty() && course.grade != "S") {
                totalGPA += course.gpa * course.credits.toInt() / totalCredits
            }
        }
    }

    private fun calculateGrade() {
        val grade: String

        if ((editingCourse?.mca!!.isEmpty() && editingCourse?.isRelative == true) || editingCourse?.obtained!!.isEmpty()) {
            editingCourse = editingCourse?.copy(
                grade = "", gpa = 0f
            )
            return
        }

        if (editingCourse?.isRelative == true) {
            grade = mcaLookup(
                round(editingCourse?.mca!!.toFloat()).toInt(),
                round(editingCourse?.obtained!!.toFloat()).toInt()
            )
        } else {
            grade = when (editingCourse?.obtained?.toFloat()!!) {
                in 90f..100f -> "A+"
                in 86f..<90f -> "A"
                in 82f..<86f -> "A-"
                in 78f..<82f -> "B+"
                in 74f..<78f -> "B"
                in 70f..<74f -> "B-"
                in 66f..<79f -> "C+"
                in 62f..<66f -> "C"
                in 58f..<62f -> "C-"
                in 54f..<58f -> "D+"
                in 50f..<54f -> "D"
                else -> "F"
            }
        }

        val gpa: Float = when (grade) {
            "A+" -> 4f
            "A" -> 4f
            "A-" -> 3.66f
            "B+" -> 3.33f
            "B" -> 3f
            "B-" -> 2.66f
            "C+" -> 2.33f
            "C" -> 2f
            "C-" -> 1.66f
            "D+" -> 1.33f
            "D" -> 1f
            else -> 0f
        }

        editingCourse = editingCourse?.copy(
            grade = grade, gpa = gpa
        )
    }

    fun deleteCourse() {
        courses.removeAt(index)
        editingCourse = null
        calculateGPA()
        updateUI()
    }
}