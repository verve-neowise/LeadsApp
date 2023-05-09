package com.neowise.leads.domain.models.inputs

import com.neowise.leads.domain.models.City
import com.neowise.leads.domain.models.Country
import com.neowise.leads.domain.models.Language
import com.neowise.leads.domain.models.LeadIntentionType

class CreateLeadInput(
    val firstName: String,
    val lastName: String?,
    val intentionType: LeadIntentionType,
    val phone: String?,
    val email: String?,
    val country: Country?,
    val city: City?,
    val languages: List<Language>
)