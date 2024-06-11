package com.avex.ragraa.data

data class Course(
    val courseName: String,
    val courseMarks: List<Section>,
    var new: Boolean = false,
    val grandTotalExists: Boolean,
)