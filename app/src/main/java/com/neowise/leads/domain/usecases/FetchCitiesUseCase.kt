package com.neowise.leads.domain.usecases

import com.neowise.leads.domain.models.City
import com.neowise.leads.domain.repositories.IOptionsRepository
import kotlinx.coroutines.flow.Flow

class FetchCitiesUseCase(
    private val optionRepository: IOptionsRepository
) {
    fun execute(countryId: Int) : Flow<List<City>> =
        optionRepository.fetchCities(countryId)
}