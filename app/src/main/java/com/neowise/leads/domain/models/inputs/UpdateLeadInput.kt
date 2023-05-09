package com.neowise.leads.domain.models.inputs

import com.neowise.leads.domain.models.*
import java.util.Date

data class UpdateLeadInput(
    val status: LeadStatus? = null,
    val propertyType: PropertyType? = null,
    val intentionType: LeadIntentionType? = null,
    val birthDate: Date? = null,
    val budget: Double? = null,
    val city: City? = null,
    val country: Country? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val languages: List<Language> = listOf(),
    val leadSource: LeadSource? = null,
    val nationality: Nationality? = null,
)