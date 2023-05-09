package com.neowise.leads.data.repositories

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.neowise.leads.util.callMutation
import com.neowise.leads.util.callQuery
import com.neowise.leads.data.mappers.toModel
import com.neowise.leads.domain.models.*
import com.neowise.leads.domain.models.inputs.CreateLeadInput
import com.neowise.leads.domain.models.inputs.UpdateLeadInput
import com.neowise.leads.domain.repositories.ILeadsRepository
import com.neowise.leads.graphql.*
import com.neowise.leads.util.optional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LeadsRepositoryApolloImpl(private val apolloClient: ApolloClient) : ILeadsRepository {

    override fun fetchLead(id: Int): Flow<Lead> =
        apolloClient.callQuery(FetchLeadQuery(id), fromCache = false)
            .map(::toModel)

    override fun fetchLeads(): Flow<List<LeadPartial>> = apolloClient
        .callQuery(FetchLeadsQuery(), fromCache = false)
        .map(::toModel)

    override fun createLead(input: CreateLeadInput): Flow<LeadPartial> =
        apolloClient.callMutation(
            CreateLeadMutation(
                input.firstName,
                input.lastName.optional(),
                input.intentionType.id,
                input.phone.optional(),
                input.email.optional(),
                input.country?.id.optional(),
                input.city?.id.optional(),
                input.languages.map { it.id }
            )
        ).map(::toModel)

    override fun updateLead(leadId: Int, input: UpdateLeadInput): Flow<Int> {

        Log.d("UPDATE-LEAD", "updateLead: $input")

        return apolloClient.callMutation(
            UpdateLeadMutation(
                leadId = leadId,
                statusId = input.status!!.id,
                propertyTypeId = input.propertyType!!.id,
                intentionId = input.intentionType?.id.optional(),
                birthDate = input.birthDate.optional(),
                budget = input.budget.optional(),
                cityId = input.city?.id.optional(),
                countryId = input.country?.id.optional(),
                firstName = input.firstName.optional(),
                lastName = input.lastName.optional(),
                languageIds = input.languages.map { it.id }.optional(),
                leadSourceId = input.leadSource?.id.optional(),
                nationalityId = input.nationality?.id.optional()
            )
        ).map {
            it.updateLead.data.status?.step ?: 1
        }
    }
}