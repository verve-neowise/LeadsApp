package com.neowise.leads.ui.screen.createLead

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neowise.leads.domain.models.*
import com.neowise.leads.domain.models.inputs.CreateLeadInput
import com.neowise.leads.domain.usecases.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed interface CreateState {
    object Loading : CreateState
    object Success : CreateState
    object Nothing : CreateState
    class Error(val message: String) : CreateState
}

class CreateLeadViewModel(
    application: Application,
    private val fetchCitiesUseCase: FetchCitiesUseCase,
    private val fetchDetailsOptionsUseCase: FetchDetailsOptionsUseCase,
    private val createLeadUseCase: CreateLeadUseCase,
) : AndroidViewModel(application) {

    val createState = MutableStateFlow<CreateState>(CreateState.Nothing)
    val detailsOptions = MutableStateFlow(DetailsOptions())
    val cities = MutableStateFlow<List<City>>(listOf())

    init {
        viewModelScope.launch {
            viewModelScope.launch {
                fetchDetailsOptionsUseCase.execute()
                    .collect {
                        detailsOptions.value = it
                    }
            }
        }
    }

    fun createLead(createLeadInput: CreateLeadInput) {

        createState.value = CreateState.Loading

        viewModelScope.launch {
            createLeadUseCase.execute(createLeadInput)
                .catch { ex ->
                    createState.value =
                        CreateState.Error(ex.message ?: "Error")
                }
                .collect {
                    createState.value = CreateState.Success
                }
        }
    }

    fun fetchCities(countryId: Int) {
        viewModelScope.launch {
            fetchCitiesUseCase.execute(countryId)
                .catch {
                }
                .collect { list ->
                    cities.value = list.sortedBy { it.title.first() }
                }
        }
    }
}