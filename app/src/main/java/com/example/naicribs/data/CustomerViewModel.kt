package com.example.naicribs.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.naicribs.models.Apartment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CustomerUiState(
    val isLoading: Boolean = false,
    val listings: List<Apartment> = emptyList(),
    val filteredListings: List<Apartment> = emptyList(),
    val savedListings: List<Apartment> = emptyList(),
    val savedIds: List<String> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val activeFilter: String = "All"
)

class CustomerViewModel : ViewModel() {

    private val listingRepo = ListingRepository()
    private val authRepo    = AuthRepository()

    private val _state = MutableStateFlow(CustomerUiState())
    val state: StateFlow<CustomerUiState> = _state

    val filterOptions = listOf("All", "Bedsitter", "1 Bedroom", "2 Bedroom", "3 Bedroom")

    init { loadAll() }

    fun loadAll() {
        viewModelScope.launch {
            _state.value = CustomerUiState(isLoading = true)
            val listings = listingRepo.getAllListings().getOrDefault(emptyList())
            val savedIds = authRepo.getFavoriteIds()
            val saved    = listingRepo.getSavedListings(savedIds).getOrDefault(emptyList())
            _state.value = CustomerUiState(
                listings        = listings,
                filteredListings = listings,
                savedListings   = saved,
                savedIds        = savedIds
            )
        }
    }

    fun toggleSave(apartmentId: String) {
        viewModelScope.launch {
            val result = authRepo.toggleFavorite(apartmentId)
            if (result.isSuccess) {
                val nowSaved = result.getOrDefault(false)
                val current  = _state.value
                val newIds   = if (nowSaved) current.savedIds + apartmentId
                else current.savedIds - apartmentId
                val newSaved = if (nowSaved) {
                    current.savedListings + (current.listings.find { it.id == apartmentId }
                        ?: return@launch)
                } else {
                    current.savedListings.filter { it.id != apartmentId }
                }
                _state.value = current.copy(savedIds = newIds, savedListings = newSaved)
            }
        }
    }

    fun search(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        applyFilters(query, _state.value.activeFilter)
    }

    fun filterByType(type: String) {
        _state.value = _state.value.copy(activeFilter = type)
        applyFilters(_state.value.searchQuery, type)
    }

    private fun applyFilters(query: String, type: String) {
        var filtered = _state.value.listings
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true) ||
                        it.houseType.contains(query, ignoreCase = true) ||
                        it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
            }
        }
        if (type != "All") filtered = filtered.filter {
            it.houseType.contains(type, ignoreCase = true)
        }
        _state.value = _state.value.copy(filteredListings = filtered)
    }
}