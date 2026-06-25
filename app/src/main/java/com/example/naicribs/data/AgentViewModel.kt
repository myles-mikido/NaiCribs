package com.example.naicribs.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.naicribs.models.Apartment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AgentUiState(
    val isLoading: Boolean = false,
    val listings: List<Apartment> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AgentViewModel : ViewModel() {

    private val listingRepo = ListingRepository()
    private val authRepo    = AuthRepository()

    private val _state = MutableStateFlow(AgentUiState())
    val state: StateFlow<AgentUiState> = _state

    init { loadMyListings() }

    fun loadMyListings() {
        val agentId = authRepo.getCurrentUserId() ?: return
        viewModelScope.launch {
            _state.value = AgentUiState(isLoading = true)
            val result = listingRepo.getAgentListings(agentId)
            _state.value = if (result.isSuccess) {
                AgentUiState(listings = result.getOrDefault(emptyList()))
            } else {
                AgentUiState(errorMessage = result.exceptionOrNull()?.message)
            }
        }
    }

    fun deleteListing(apartmentId: String) {
        viewModelScope.launch {
            val result = listingRepo.deleteListing(apartmentId)
            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    listings = _state.value.listings.filter { it.id != apartmentId },
                    successMessage = "Listing deleted"
                )
            } else {
                _state.value = _state.value.copy(
                    errorMessage = result.exceptionOrNull()?.message ?: "Delete failed"
                )
            }
        }
    }

    fun getCurrentUserId() = authRepo.getCurrentUserId()
    fun clearMessages() { _state.value = _state.value.copy(errorMessage = null, successMessage = null) }
}