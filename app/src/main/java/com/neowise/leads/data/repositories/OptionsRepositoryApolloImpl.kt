package com.neowise.leads.data.repositories

import com.apollographql.apollo3.ApolloClient
import com.neowise.leads.util.callQuery
import com.neowise.leads.data.mappers.toModel
import com.neowise.leads.domain.models.City
import com.neowise.leads.domain.repositories.IOptionsRepository
import com.neowise.leads.graphql.CitiesQuery
import com.neowise.leads.graphql.FetchDetailsOptionsQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OptionsRepositoryApolloImpl(
    private val apolloClient: ApolloClient
) : IOptionsRepository {
    override fun fetchDetailsOptions() =
        apolloClient.callQuery(FetchDetailsOptionsQuery(), true)
            .map(::toModel)

    override fun fetchCities(countryId: Int): Flow<List<City>> =
        apolloClient.callQuery(CitiesQuery(countryId), fromCache = true)
            .map(::toModel)
}
