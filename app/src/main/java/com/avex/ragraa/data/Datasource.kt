package com.avex.ragraa.data

import androidx.compose.ui.graphics.Color
import org.jsoup.Jsoup

data class Marks(
    val weightage: Float,
    val obtained: Float,
    val total: Float,
    val average: Float,
    val minimum: Float,
    val maximum: Float,
    val color: Color
)

data class Section(val name: String, val listOfMarks: List<Marks>)

data class Course(val courseName: String, val courseMarks: List<Section>)

object Datasource {
    var flexResponse = ""
    val Database: MutableList<Course> = mutableListOf()

    fun parseHTML() {
        val htmlFile = Jsoup.parse(flexResponse).body()
        val totalCourses = htmlFile.getElementsByAttributeValue("id", "accordion")

        for (course in totalCourses) {
            val listOfItems: MutableList<Section> = mutableListOf()

            for ((i, courseWork) in course.getElementsByClass("mb-0").withIndex()) {
                if (i == course.getElementsByClass("mb-0").size - 1) continue

                val listOfMarks: MutableList<Marks> = mutableListOf()

                for (courseWorkMerit in course.getElementsByClass("sum_table table m-table m-table--head-bg-info table-bordered table-striped table-responsive")[i].getElementsByClass(
                    "calculationrow"
                )) {
                    val temp: MutableList<Float> = mutableListOf()
                    for ((v, individualItem) in courseWorkMerit.getElementsByClass("text-center")
                        .withIndex()) {
                        if (v == 0) continue

                        val thing: Float = when (individualItem.text()) {
                            "", "-" -> (-1).toFloat()
                            else -> individualItem.text().toFloat()
                        }

                        temp.add(thing)
                    }
                    listOfMarks.add(
                        Marks(
                            temp[0],
                            temp[1],
                            temp[2],
                            temp[3],
                            temp[5],
                            temp[6],
                            if (temp[2].toInt() <= 0 || temp[1].toInt() <= 0) Color.White else Color.hsl(
                                ((100.8 * temp[1] / temp[2]).toFloat()),
                                0.78F,
                                0.75F
                            )
                        )
                    )
                }

                listOfItems.add(Section(courseWork.text(), listOfMarks))
            }

            Database.add(Course(course.getElementsByTag("h5")[0].text(), listOfItems))
        }
    }
}