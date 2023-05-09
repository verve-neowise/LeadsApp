package com.neowise.leads.util

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.flow

class NetworkException(message: String) : Exception(message)

fun <D: Query.Data> ApolloClient.callQuery(query: Query<D>, fromCache: Boolean = true) = flow {

    val fetchPolicy = if (fromCache) FetchPolicy.CacheFirst else FetchPolicy.NetworkOnly

    val response = query(query)
        .fetchPolicy(fetchPolicy)
        .execute()

    if (response.hasErrors()) {
        val message = response.errors?.joinToString(";") { it.message }
        throw NetworkException(message ?: "Error on request")
    } else if (response.data != null) {
        emit(response.dataAssertNoErrors)
    }
}

fun <D: Mutation.Data> ApolloClient.callMutation(mutation: Mutation<D>) = flow {

    val response = mutation(mutation)
        .fetchPolicy(FetchPolicy.NetworkOnly)
        .execute()

    if (response.hasErrors()) {
        val message = response.errors?.joinToString(";") { it.message }
        throw NetworkException(message ?: "Error on request")
    } else if (response.data != null) {
        emit(response.dataAssertNoErrors)
    }
}