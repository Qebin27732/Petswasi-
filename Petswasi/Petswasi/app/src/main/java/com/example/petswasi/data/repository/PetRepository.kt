package com.example.petswasi.data.repository

import com.example.petswasi.data.model.Pet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class PetRepository {
    private val database = FirebaseDatabase.getInstance("https://petswasi-68ef9-default-rtdb.firebaseio.com/")
    private val petsRef = database.getReference("pets")

    fun listenPets(onUpdate: (List<Pet>) -> Unit) {
        petsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val petsList = mutableListOf<Pet>()
                for (child in snapshot.children) {
                    child.getValue(Pet::class.java)?.let { petsList.add(it) }
                }
                onUpdate(petsList)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    suspend fun getAllPets(): List<Pet> {
        return try {
            val snapshot = petsRef.get().await()
            val petsList = mutableListOf<Pet>()
            for (child in snapshot.children) {
                child.getValue(Pet::class.java)?.let { petsList.add(it) }
            }
            petsList
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addPet(pet: Pet) {
        try {
            val newPetRef = petsRef.push()
            val petWithId = pet.copy(id = newPetRef.key ?: "")
            newPetRef.setValue(petWithId).await()
        } catch (e: Exception) {
            // Manejar error
        }
    }
}
