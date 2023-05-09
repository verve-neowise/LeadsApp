package com.neowise.leads.domain.models

data class City(
    val id: Int,
    val title: String
) {
    override fun toString(): String {
        return title
    }
}