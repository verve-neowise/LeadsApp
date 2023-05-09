package com.neowise.leads.ui.screen.leadProfile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neowise.leads.domain.models.City
import com.neowise.leads.domain.models.DetailsOptions
import com.neowise.leads.domain.models.Lead
import com.neowise.leads.domain.models.inputs.UpdateLeadInput
import com.neowise.leads.domain.usecases.FetchCitiesUseCase
import com.neowise.leads.domain.usecases.FetchDetailsOptionsUseCase
import com.neowise.leads.domain.usecases.FetchSingleLeadUseCase
import com.neowise.leads.domain.usecases.UpdateLeadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed interface State<T> {
    class Loading<T> : State<T>
    class Success<T>(val data: T) : State<T>
    class Error<T>(val message: String) : State<T>
}

class LeadProfileViewModel(
    application: Application,
    private val fetchSingleLeadUseCase: FetchSingleLeadUseCase,
    private val fetchDetailsOptionsUseCase: FetchDetailsOptionsUseCase,
    private val fetchCitiesUseCase: FetchCitiesUseCase,
    private val updateLeadUseCase: UpdateLeadUseCase
) : AndroidViewModel(application) {

    var leadId: Int = -1

    val processStatus = MutableStateFlow<State<String>>(State.Loading())

    val leadState = MutableStateFlow<State<Lead>>(State.Loading())
    val detailsOptions = MutableStateFlow<State<DetailsOptions>>(State.Loading())
    val cities = MutableStateFlow<State<List<City>>>(State.Success(listOf()))

    var defaultCity: City? = null

    init {
        viewModelScope.launch {
            fetchDetailsOptionsUseCase.execute()
                .catch {
                    detailsOptions.value = State.Error(it.message ?: "Error")
                }
                .collect {
                    detailsOptions.value = State.Success(it)
                }
        }
    }

    fun updateLead(data: String, input: UpdateLeadInput) {
        viewModelScope.launch {

            processStatus.value = State.Loading()

            updateLeadUseCase.execute(leadId, input)
                .catch {
                    processStatus.value = State.Error("$data: ${it.message}")
                }
                .collect {
                    processStatus.value = State.Success(data)
                }
        }
    }

    fun fetchCities(countryId: Int, defaultCity: City? = null) {
        this.defaultCity = defaultCity
        viewModelScope.launch {
            fetchCitiesUseCase.execute(countryId)
                .catch {
                    Log.e("TAG", ": ${it.message}")
                }
                .collect { list ->
                    cities.value = State.Success(
                        list.sortedBy { it.title.first() }
                    )
                }
        }
    }

    fun fetchLead(id: Int) {

        leadId = id

        leadState.value = State.Loading()

        viewModelScope.launch {
            fetchSingleLeadUseCase.execute(id)
                .catch {
                    leadState.value = State.Error(it.message!!)
                }
                .collect {
                    leadState.value = State.Success(it)
                }
        }
    }

    fun errorProcessStatus(message: String) {
        processStatus.value = State.Error(message)
    }
}