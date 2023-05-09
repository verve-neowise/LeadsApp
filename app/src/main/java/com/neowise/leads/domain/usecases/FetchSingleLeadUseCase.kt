package com.neowise.leads.domain.usecases

import com.neowise.leads.domain.models.Lead
import com.neowise.leads.domain.repositories.ILeadsRepository
import kotlinx.coroutines.flow.Flow

class FetchSingleLeadUseCase(private val leadsRepository: ILeadsRepository) {
    fun execute(id: Int) : Flow<Lead> = leadsRepository.fetchLead(id)
}