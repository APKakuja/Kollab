package com.example.kollab.perfilesusers

    fun PerfilDTO.toPerfil(): Perfil {
        return Perfil(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            genero = genero,
            puesto = puesto,
            skills = skills,
            experiencia = experiencia,
            fotoUrl = fotoUrl,
            localizacion = localizacion,
            edad = edad
        )
    }

