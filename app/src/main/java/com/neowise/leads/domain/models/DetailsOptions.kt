package com.neowise.leads.domain.models

data class DetailsOptions(
    val leadIntentionTypes: List<LeadIntentionType> = listOf(),
    val adSources: List<AdSource> = listOf(),
    val webSources: List<WebSource> = listOf(),
    val channelSources: List<ChannelSource> = listOf(),
    val propertyTypes: List<PropertyType> = listOf(),
    val countries: List<Country> = listOf(),
    val leadStatus: List<LeadStatus> = listOf(),
    val nationalities: List<Nationality> = listOf(),
    val languages: List<Language> = listOf()
)