package com.neowise.leads.data.mappers

import com.neowise.leads.domain.models.*
import com.neowise.leads.graphql.CreateLeadMutation
import com.neowise.leads.graphql.FetchLeadQuery
import com.neowise.leads.graphql.FetchLeadsQuery
import com.neowise.leads.util.localDateTime
import com.neowise.leads.util.mapTo
import com.neowise.leads.util.toFormat

fun toModel(data: FetchLeadQuery.Data) =
    data.fetchLead!!.data.mapTo { lead ->
        Lead(
            id = lead.id,
            displayName = lead.displayName,
            firstName = lead.firstName,
            secondName = lead.secondName,
            lastName = lead.lastName,
            budget = lead.budget,
            avatar = lead.avatar?.path,
            country = lead.country?.mapTo {
                Country(
                    id = it.id,
                    emoji = it.emoji,
                    title = it.title,
                    phoneCode = "+${it.phoneCode}",
                    shortCode1 = it.shortCode1,
                    shortCode2 = it.shortCode2
                )
            },
            city = lead.city?.mapTo {
                City(
                    id = it.id,
                    title = it.title
                )
            },
            birthday = lead.birthDate?.localDateTime()?.toFormat(),
            intention = lead.intention?.mapTo {
                LeadIntentionType(it.id, it.title)
            },
            languages = lead.languages?.map {
                Language(id = it.id, code = it.shortCode, title = it.title)
            } ?: listOf(),
            nationality = lead.nationality?.mapTo {
                Nationality(
                    it.id,
                    it.title
                )
            },
            propertyType = lead.propertyType?.mapTo {
                PropertyType(it.id, it.title)
            },
            status = lead.status?.mapTo {
                LeadStatus(
                    id = it.id,
                    title = it.title,
                    color = it.color,
                    step = it.step,
                    stepsCount = it.stepsCount
                )
            },
            displaySource = lead.displaySource,
            adSource = lead.adSource?.mapTo { it ->
                AdSource(id = it.id, title = it.title)
            },
            channelSource = lead.channelSource?.mapTo { it ->
                ChannelSource(id = it.id, title = it.title)
            },
            webSource = lead.webSource?.mapTo { it ->
                WebSource(id = it.id, title = it.title, url = it.url)
            },
        )
    }


fun toModel(data: FetchLeadsQuery.Data) =
    data.fetchLeads.data.map { lead ->
        LeadPartial(
            id = lead.id,
            displayName = lead.displayName,
            avatar = lead.avatar?.path,
            countryEmoji = lead.country?.emoji,
            intention = lead.intention?.title,
            adSource = lead.adSource?.title,
            channelSource = lead.channelSource?.title,
            webSource = lead.webSource?.title,
            source = lead.source?.title,
            status = lead.status?.title,
            createdAt = lead.createdAt?.localDateTime(),
            updatedAt = lead.updatedAt?.localDateTime(),
        )
    }

fun toModel(data: CreateLeadMutation.Data) =
    data.createLead.data.mapTo { lead ->
        LeadPartial(
            id = lead.id,
            displayName = lead.displayName,
            avatar = lead.avatar?.path,
            countryEmoji = lead.country?.emoji,
            intention = lead.intention?.title,
            adSource = lead.adSource?.title,
            channelSource = lead.channelSource?.title,
            webSource = lead.webSource?.title,
            source = lead.source?.title,
            status = lead.status?.title,
            createdAt = lead.createdAt?.localDateTime(),
            updatedAt = lead.updatedAt?.localDateTime(),
        )
    }