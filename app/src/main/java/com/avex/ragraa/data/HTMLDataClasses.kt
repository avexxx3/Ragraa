package com.avex.ragraa.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class marksHTML(val html: String = "", @Id var id: Long = 0)

@Entity
data class attendanceHTML(val html: String = "", @Id var id: Long = 0)

@Entity
data class transcriptHTML(val html: String = "", @Id var id: Long = 0)