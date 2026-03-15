package com.example.kollab.perfilesusers

data class Perfil(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val genero: String,
    val puesto: String,
    val skills: String,
    val experiencia: String,
    val localizacion: String,
    val fotoUrl: String,
    val edad: Int
)
