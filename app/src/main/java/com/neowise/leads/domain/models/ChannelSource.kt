package com.neowise.leads.domain.models

data class ChannelSource(
    val id: Int,
    val title: String
) {
    override fun toString(): String {
        return title
    }
}