package com.neowise.leads.data.mappers

import android.util.Log
import com.neowise.leads.domain.models.*
import com.neowise.leads.graphql.FetchDetailsOptionsQuery
import com.neowise.leads.domain.models.DetailsOptions
import com.neowise.leads.graphql.CitiesQuery


fun toModel(detailsOptions: FetchDetailsOptionsQuery.Data): DetailsOptions {

    Log.d("LEADS", "toModel: ${detailsOptions.fetchLeadIntentionTypes.map { it.title }}")

    return DetailsOptions(
        leadIntentionTypes = detailsOptions.fetchLeadIntentionTypes.map { LeadIntentionType(it.id, it.title) },
        adSources = detailsOptions.fetchAdSources.map { AdSource(it.id, it.title) },
        webSources = detailsOptions.fetchWebSources.map { WebSource(it.id, it.title, it.url) },
        channelSources = detailsOptions.fetchChannelSources.map { ChannelSource(it.id, it.title) },
        propertyTypes = detailsOptions.fetchPropertyTypes.map { PropertyType(it.id, it.title) },
        countries = detailsOptions.fetchCountries.map {
            Country(
                id = it.id,
                emoji = it.emoji,
                title = it.title,
                phoneCode = "+${it.phoneCode}",
                shortCode1 = it.shortCode1,
                shortCode2 = it.shortCode2
            )
        },
        leadStatus = detailsOptions.fetchLeadStatusTypes.map {
            LeadStatus(
                id = it.id,
                color = it.color,
                title = it.title,
                stepsCount = it.stepsCount,
                step = it.step
            )
        },
        nationalities = detailsOptions.nationalities.map { Nationality(it.id, it.title) },
        languages = detailsOptions.languages.map {
            Language(
                id = it.id,
                title = it.title,
                code = it.shortCode
            )
        }
    )
}

fun toModel(data: CitiesQuery.Data): List<City> {
    return data.cities.map {
        City(
            id = it.id,
            title = it.title
        )
    }
}