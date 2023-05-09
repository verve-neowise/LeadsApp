package com.neowise.leads.data.client

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.neowise.leads.BuildConfig

object ApolloFactory {

    private val sqlNormalizedCacheFactory = SqlNormalizedCacheFactory("apollo.db")

    val apolloClient = ApolloClient.Builder()
        .serverUrl(BuildConfig.SERVER_URL)
        .addHttpHeader("Authorization", "Bearer ${BuildConfig.TOKEN}")
        .addHttpInterceptor(LoggingInterceptor(level = LoggingInterceptor.Level.BODY) {
            Log.d("APOLLO", it)
        })
        .normalizedCache(sqlNormalizedCacheFactory)
        .build()
}