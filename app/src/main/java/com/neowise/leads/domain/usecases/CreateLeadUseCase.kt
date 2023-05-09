package com.neowise.leads.domain.usecases

import com.neowise.leads.domain.models.*
import com.neowise.leads.domain.models.inputs.CreateLeadInput
import com.neowise.leads.domain.repositories.ILeadsRepository
import kotlinx.coroutines.flow.Flow

class CreateLeadUseCase(
    private val leadsRepository: ILeadsRepository
) {
    fun execute(
        input: CreateLeadInput
    ): Flow<LeadPartial> {
        return leadsRepository.createLead(input)
    }
}