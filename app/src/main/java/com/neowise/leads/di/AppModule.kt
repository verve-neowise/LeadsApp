package com.neowise.leads.di

import org.koin.dsl.module

val appModule = module {
    includes(networkModule, useCaseModule, viewModelModule)
}