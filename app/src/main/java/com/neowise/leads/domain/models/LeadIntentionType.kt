package com.neowise.leads.domain.models

data class LeadIntentionType(
    val id: Int,
    val title: String
) {
    override fun toString(): String {
        return title
    }
}