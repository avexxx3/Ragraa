package com.avex.ragraa.data.dataclasses

data class Section(
    val name: String,
    val listOfMarks: List<Marks>,
    var obtained: Float,
    val total: Float,
    val average: Float,
    var new: Boolean = false,
    var bestOf: Int = 0,
    var selectedList: List<Marks> = listOf(),
) {
    init {
        bestOf = listOfMarks.size
        selectedList = listOfMarks
    }

    private fun updateDetails() {
        var totalAbs = 0f
        var totalWgt = 0f
        selectedList.forEach {
            totalAbs += it.weightage * it.obtained / it.total
            totalWgt += it.weightage
        }
        obtained = (totalAbs) * total / totalWgt
    }

    fun increment() {
        if (bestOf == listOfMarks.size)
            return

        bestOf++

        if (bestOf == listOfMarks.size)
            selectedList = listOfMarks
        else
            selectedList = listOfMarks.sortedBy { it.obtained.toFloat() / it.total }.reversed()
                .subList(0, bestOf)

        updateDetails()
    }

    fun decrement() {
        if (bestOf == 1)
            return
        bestOf--

        selectedList = listOfMarks.sortedBy { it.obtained / it.total }.reversed().subList(0, bestOf)

        updateDetails()
    }
}