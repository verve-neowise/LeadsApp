package com.neowise.leads.domain.usecases

import com.neowise.leads.domain.repositories.IOptionsRepository
import com.neowise.leads.domain.models.DetailsOptions
import kotlinx.coroutines.flow.Flow

class FetchDetailsOptionsUseCase(
    private val optionsRepository: IOptionsRepository
) {
    fun execute() : Flow<DetailsOptions> = optionsRepository.fetchDetailsOptions()
}