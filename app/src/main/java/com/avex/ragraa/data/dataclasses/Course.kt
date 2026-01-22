package com.avex.ragraa.data.dataclasses

data class Course(
    val name: String,
    val marks: List<Section>,
    var newMarks: Boolean = false,
    val grandTotalExists: Boolean,
    val attendance: List<Attendance>,
    var attendancePercentage: Float,
    val attendanceAbsents: Int
) {
    fun updateTotal() {
        val gTotal = marks.last()
        gTotal.obtained = 0f
        for(item in marks) {
            if(item != gTotal)
                gTotal.obtained += item.obtained
        }
    }
}
