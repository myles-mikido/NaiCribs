package com.example.naicribs.data

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.naicribs.models.Apartment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class AddListingUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val uploadedImageUrls: List<String> = emptyList(),
    val pinnedLatitude: Double? = null,
    val pinnedLongitude: Double? = null,
    val pinnedLocationLabel: String = ""
)

class AddListingViewModel : ViewModel() {

    private val listingRepo = ListingRepository()
    private val authRepo    = AuthRepository()

    private val _state = MutableStateFlow(AddListingUiState())
    val state: StateFlow<AddListingUiState> = _state

    fun setLocation(lat: Double, lng: Double, label: String) {
        _state.value = _state.value.copy(
            pinnedLatitude = lat, pinnedLongitude = lng, pinnedLocationLabel = label
        )
    }

    fun uploadImage(context: Context, uri: Uri) {
        val agentId = authRepo.getCurrentUserId() ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
            if (bytes == null) {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Could not read image")
                return@launch
            }
            val result = listingRepo.uploadImage(agentId, bytes)
            _state.value = if (result.isSuccess) {
                val urls = _state.value.uploadedImageUrls + result.getOrDefault("")
                _state.value.copy(isLoading = false, uploadedImageUrls = urls)
            } else {
                _state.value.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message)
            }
        }
    }

    fun removeImage(url: String) {
        _state.value = _state.value.copy(
            uploadedImageUrls = _state.value.uploadedImageUrls.filter { it != url }
        )
    }

    fun submitListing(
        name: String, description: String, price: String,
        location: String, contactNumber: String, email: String,
        houseType: String,
        tags: List<String> = emptyList()   // ← area tags
    ) {
        if (name.isBlank() || price.isBlank() || location.isBlank() || contactNumber.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Please fill in all required fields")
            return
        }
        val agentId = authRepo.getCurrentUserId() ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val apartment = Apartment(
                id            = UUID.randomUUID().toString(),
                agentId       = agentId,
                name          = name.trim(),
                description   = description.trim(),
                price         = price.toDoubleOrNull() ?: 0.0,
                location      = location.trim(),
                latitude      = _state.value.pinnedLatitude,
                longitude     = _state.value.pinnedLongitude,
                contactNumber = contactNumber.trim(),
                email         = email.trim(),
                houseType     = houseType,
                imageUrls     = _state.value.uploadedImageUrls,
                tags          = tags.map { it.trim() }.filter { it.isNotBlank() }
            )
            val result = listingRepo.addListing(apartment)
            _state.value = if (result.isSuccess) {
                AddListingUiState(isSuccess = true)
            } else {
                _state.value.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message)
            }
        }
    }

    fun clearError() { _state.value = _state.value.copy(errorMessage = null) }
}