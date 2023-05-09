package com.neowise.leads.ui.screen.leadsList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neowise.leads.domain.models.LeadPartial
import com.neowise.leads.domain.usecases.FetchLeadsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed interface LeadListState {
    object Loading : LeadListState
    class Success(val leads: List<LeadPartial> = listOf()) : LeadListState
    class Error(val message: String): LeadListState
}

class LeadListViewModel(
    application: Application,
    private val fetchLeadsUseCase: FetchLeadsUseCase
) : AndroidViewModel(application) {

    private val _uiState: MutableStateFlow<LeadListState> =
        MutableStateFlow(LeadListState.Success())

    val uiState: StateFlow<LeadListState> = _uiState

    fun fetchLeads() {
        _uiState.value = LeadListState.Loading

        viewModelScope.launch {
            fetchLeadsUseCase.execute()
                .catch {
                    _uiState.value = LeadListState.Error(it.message ?: "Some Error")
                }
                .collect {
                    _uiState.value = LeadListState.Success(it)
                }
        }
    }
}