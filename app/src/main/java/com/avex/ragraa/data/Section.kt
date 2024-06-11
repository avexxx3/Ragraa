package com.avex.ragraa.data

data class Section(
    val name: String,
    val listOfMarks: List<Marks>,
    val obtained: Float,
    val total: Float,
    val average: Float,
    var new: Boolean = false,
)