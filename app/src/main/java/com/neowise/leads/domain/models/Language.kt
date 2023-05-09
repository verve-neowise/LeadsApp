package com.neowise.leads.domain.models

data class Language(
    val id: Int,
    val code: String,
    val title: String
) {
    override fun toString(): String {
        return title
    }
}