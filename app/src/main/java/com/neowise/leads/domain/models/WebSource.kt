package com.neowise.leads.domain.models

data class WebSource(
    val id: Int,
    val title: String,
    val url: String
) {
    override fun toString(): String {
        return title
    }
}