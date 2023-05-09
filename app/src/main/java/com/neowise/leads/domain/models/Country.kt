package com.neowise.leads.domain.models

data class Country(
    val id: Int,
    val emoji: String?,
    val phoneCode: String,
    val shortCode1: String,
    val shortCode2: String,
    val title: String
) {
    override fun toString(): String {
        return "${emoji ?: ""}$title"
    }
}