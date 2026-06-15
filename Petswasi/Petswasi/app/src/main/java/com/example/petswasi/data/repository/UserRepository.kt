package com.example.petswasi.data.repository

import com.example.petswasi.data.model.Donation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

class UserRepository {
    private val database = FirebaseDatabase.getInstance("https://petswasi-68ef9-default-rtdb.firebaseio.com/")
    private val usersRef = database.getReference("usuarios")

    fun createUsuario(uid: String, email: String, nombre: String, contrasena: String) {
        val nuevoUsuario = mapOf(
            "email" to email,
            "nombre" to nombre,
            "contrasena" to contrasena
        )
        usersRef.child(uid).setValue(nuevoUsuario)
    }

    fun enviarMensaje(fundacion: String, mensaje: String, usuarioNombre: String) {
        val mensajeData = mapOf(
            "fundacion" to fundacion,
            "mensaje" to mensaje,
            "usuario" to usuarioNombre,
            "fecha" to System.currentTimeMillis()
        )
        database.getReference("mensajes").push().setValue(mensajeData)
    }

    fun registrarDonacion(monto: String, usuarioNombre: String) {
        val donacionData = mapOf(
            "monto" to monto,
            "usuario" to usuarioNombre,
            "fecha" to System.currentTimeMillis()
        )
        database.getReference("donaciones").push().setValue(donacionData)
    }

    fun registrarAdopcion(petName: String, usuarioNombre: String) {
        val adopcionData = mapOf(
            "mascota" to petName,
            "usuario" to usuarioNombre,
            "fecha" to System.currentTimeMillis()
        )
        database.getReference("adopciones").push().setValue(adopcionData)
    }

    fun listenDonaciones(onUpdate: (List<Donation>) -> Unit) {
        database.getReference("donaciones").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Donation>()
                for (child in snapshot.children) {
                    child.getValue(Donation::class.java)?.let { list.add(it) }
                }
                onUpdate(list.reversed())
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    suspend fun login(email: String, contrasena: String): String? {
        return try {
            // Añadimos un timeout de 5 segundos para que no se quede cargando infinito
            withTimeoutOrNull(5000) {
                val snapshot = usersRef.get().await()
                for (userSnapshot in snapshot.children) {
                    val dbEmail = userSnapshot.child("email").getValue(String::class.java)
                    val dbPass = userSnapshot.child("contrasena").getValue(String::class.java)
                    val dbNombre = userSnapshot.child("nombre").getValue(String::class.java)
                    
                    if (dbEmail?.trim() == email.trim() && dbPass == contrasena) {
                        return@withTimeoutOrNull dbNombre ?: "Usuario"
                    }
                }
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
