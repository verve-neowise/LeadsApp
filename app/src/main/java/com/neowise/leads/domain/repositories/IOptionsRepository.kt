package com.neowise.leads.domain.repositories

import com.neowise.leads.domain.models.City
import com.neowise.leads.domain.models.DetailsOptions
import kotlinx.coroutines.flow.Flow


interface IOptionsRepository  {
    fun fetchDetailsOptions() : Flow<DetailsOptions>
    fun fetchCities(countryId: Int): Flow<List<City>>
}