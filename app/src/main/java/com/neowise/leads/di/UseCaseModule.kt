package com.neowise.leads.di

import com.neowise.leads.domain.usecases.*
import org.koin.dsl.module

val useCaseModule = module {
    factory { FetchLeadsUseCase(leadsRepository = get()) }
    factory { FetchCitiesUseCase(optionRepository = get()) }
    factory { CreateLeadUseCase(leadsRepository = get()) }
    factory { FetchSingleLeadUseCase(leadsRepository = get()) }
    factory { FetchDetailsOptionsUseCase(optionsRepository = get()) }
    factory { UpdateLeadUseCase(leadsRepository = get()) }
}