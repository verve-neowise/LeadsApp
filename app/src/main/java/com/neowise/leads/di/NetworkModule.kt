package com.neowise.leads.di

import com.neowise.leads.data.client.ApolloFactory
import com.neowise.leads.data.repositories.*
import com.neowise.leads.domain.repositories.*
import org.koin.dsl.module

val networkModule = module {
    single { ApolloFactory.apolloClient }

    single<ILeadsRepository> { LeadsRepositoryApolloImpl(get()) }
    single<IOptionsRepository> { OptionsRepositoryApolloImpl(get()) }
}