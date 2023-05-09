package com.neowise.leads.domain.repositories

import com.neowise.leads.domain.models.*
import com.neowise.leads.domain.models.inputs.CreateLeadInput
import com.neowise.leads.domain.models.inputs.UpdateLeadInput
import kotlinx.coroutines.flow.Flow

interface ILeadsRepository {

    fun fetchLead(id: Int): Flow<Lead>

    fun fetchLeads(): Flow<List<LeadPartial>>

    fun createLead(input: CreateLeadInput): Flow<LeadPartial>

    fun updateLead(leadId: Int, input: UpdateLeadInput): Flow<Int>
}