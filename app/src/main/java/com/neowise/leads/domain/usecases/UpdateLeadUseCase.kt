package com.neowise.leads.domain.usecases

import android.util.Log
import com.neowise.leads.domain.models.inputs.UpdateLeadInput
import com.neowise.leads.domain.repositories.ILeadsRepository
import kotlinx.coroutines.flow.Flow

class UpdateLeadUseCase(
    private val leadsRepository: ILeadsRepository
) {
    fun execute(
        leadId: Int,
        input: UpdateLeadInput
    ): Flow<Int> {
        Log.d("UpdateLeadUseCase", "execute: $input")
        return leadsRepository.updateLead(leadId, input)
    }
}