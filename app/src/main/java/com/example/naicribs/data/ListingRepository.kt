package com.example.naicribs.data

import com.example.naicribs.models.Apartment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ListingRepository {

    private val db      = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val col get() = db.collection("apartments")

    suspend fun getAllListings(): Result<List<Apartment>> = runCatching {
        col.orderBy("createdAt", Query.Direction.DESCENDING).get().await()
            .documents.mapNotNull { it.toObject(Apartment::class.java)?.copy(id = it.id) }
    }

    suspend fun getAgentListings(agentId: String): Result<List<Apartment>> = runCatching {
        col.whereEqualTo("agentId", agentId).get().await()
            .documents.mapNotNull { it.toObject(Apartment::class.java)?.copy(id = it.id) }
            .sortedByDescending { it.createdAt }
    }

    suspend fun getSavedListings(ids: List<String>): Result<List<Apartment>> = runCatching {
        if (ids.isEmpty()) return@runCatching emptyList()
        // Firestore whereIn supports max 10 per query; chunk if needed
        ids.chunked(10).flatMap { chunk ->
            col.whereIn(com.google.firebase.firestore.FieldPath.documentId(), chunk)
                .get().await()
                .documents.mapNotNull { it.toObject(Apartment::class.java)?.copy(id = it.id) }
        }
    }

    suspend fun addListing(apartment: Apartment): Result<Unit> = runCatching {
        val id  = apartment.id.ifBlank { UUID.randomUUID().toString() }
        val map = hashMapOf(
            "agentId"       to apartment.agentId,
            "name"          to apartment.name,
            "description"   to apartment.description,
            "price"         to apartment.price,
            "location"      to apartment.location,
            "latitude"      to apartment.latitude,
            "longitude"     to apartment.longitude,
            "houseType"     to apartment.houseType,
            "contactNumber" to apartment.contactNumber,
            "email"         to apartment.email,
            "imageUrls"     to apartment.imageUrls,
            "tags"          to apartment.tags,          // ← new
            "createdAt"     to com.google.firebase.Timestamp.now()
        )
        col.document(id).set(map).await()
        Unit
    }

    suspend fun updateListing(apartment: Apartment): Result<Unit> = runCatching {
        val map = hashMapOf(
            "name"          to apartment.name,
            "description"   to apartment.description,
            "price"         to apartment.price,
            "location"      to apartment.location,
            "latitude"      to apartment.latitude,
            "longitude"     to apartment.longitude,
            "houseType"     to apartment.houseType,
            "contactNumber" to apartment.contactNumber,
            "email"         to apartment.email,
            "imageUrls"     to apartment.imageUrls,
            "tags"          to apartment.tags           // ← new
        )
        col.document(apartment.id).update(map as Map<String, Any?>).await()
        Unit
    }

    suspend fun deleteListing(id: String): Result<Unit> = runCatching {
        col.document(id).delete().await()
        Unit
    }

    suspend fun uploadImage(agentId: String, bytes: ByteArray): Result<String> = runCatching {
        val path = "apartment-images/$agentId/${UUID.randomUUID()}.jpg"
        val ref  = storage.reference.child(path)
        ref.putBytes(bytes).await()
        ref.downloadUrl.await().toString()
    }

    // Client-side search + tag filter (fine for small/medium app)
    suspend fun searchListings(query: String): Result<List<Apartment>> = runCatching {
        val all = col.get().await()
            .documents.mapNotNull { it.toObject(Apartment::class.java)?.copy(id = it.id) }
        all.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.location.contains(query, ignoreCase = true) ||
                    it.houseType.contains(query, ignoreCase = true) ||
                    it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }
    }
}