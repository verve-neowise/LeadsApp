package com.neowise.leads.domain.usecases

import com.neowise.leads.domain.models.LeadPartial
import com.neowise.leads.domain.repositories.ILeadsRepository
import kotlinx.coroutines.flow.Flow

class FetchLeadsUseCase(private val leadsRepository: ILeadsRepository) {
    fun execute() : Flow<List<LeadPartial>> = leadsRepository.fetchLeads()
}