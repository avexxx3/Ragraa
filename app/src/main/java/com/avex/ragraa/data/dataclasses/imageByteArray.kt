package com.avex.ragraa.data.dataclasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class imageByteArray(var byteArray: ByteArray, var rollNo: String, @Id var id: Long)
