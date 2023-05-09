package com.neowise.leads.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

fun Date.localDateTime(): LocalDateTime? {
    return toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun LocalDateTime.toFormat(): String = format(dateTimeFormatter)

fun String.parseDate(): Date? {
    return try {
        dateFormatter.parse(this)
    }
    catch (e: Exception) {
        null
    }
}