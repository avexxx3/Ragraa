package com.avex.ragraa.data.dataclasses

data class TranscriptCourse(
    val courseID: String,
    val courseName: String,
    val creditHours: Int,
    val grade: String,
    val gpa: Float,
    val isRelative: Boolean
)