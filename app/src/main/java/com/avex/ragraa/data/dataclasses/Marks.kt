package com.avex.ragraa.data.dataclasses

data class Marks(
    val number: Int,
    val weightage: Float,
    val obtained: Float,
    val total: Float,
    val average: Float,
    val minimum: Float,
    val maximum: Float,
    var new: Boolean = false
)