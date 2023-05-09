package com.neowise.leads.domain.models

import java.time.LocalDateTime

data class LeadPartial(
    val id: Int,
    val displayName: String?,
    val avatar: String?,
    val countryEmoji: String?,
    val intention: String?,
    val adSource: String?,
    val channelSource: String?,
    val webSource: String?,
    val source: String?,
    val status: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)