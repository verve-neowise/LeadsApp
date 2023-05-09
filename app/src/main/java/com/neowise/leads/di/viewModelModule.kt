package com.neowise.leads.di

import com.neowise.leads.ui.screen.createLead.CreateLeadViewModel
import com.neowise.leads.ui.screen.leadProfile.LeadProfileViewModel
import com.neowise.leads.ui.screen.leadsList.LeadListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { LeadListViewModel(application = get(), fetchLeadsUseCase = get()) }

    viewModel {
        CreateLeadViewModel(
            application = get(),
            fetchCitiesUseCase = get(),
            fetchDetailsOptionsUseCase = get(),
            createLeadUseCase = get()
        )
    }

    viewModel {
        LeadProfileViewModel(
            application = get(),
            fetchSingleLeadUseCase = get(),
            fetchDetailsOptionsUseCase = get(),
            fetchCitiesUseCase = get(),
            updateLeadUseCase = get()
        )
    }
}