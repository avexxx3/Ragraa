package com.avex.ragraa.data

data class CourseAttendance(
    val courseName: String,
    var percentage: Float,
    val attendance: List<Attendance>,
    val absents: Int
)
