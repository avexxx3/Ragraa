package com.avex.ragraa.data

data class CourseMarks(
    val courseName: String,
    val courseMarks: List<Section>,
    var new: Boolean = false,
    val grandTotalExists: Boolean,
)