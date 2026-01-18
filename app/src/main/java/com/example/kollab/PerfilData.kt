package com.example.kollab

import Perfil

object PerfilData {


    val perfiles = listOf(
        Perfil(
            nombre = "Antonio Lovato",
            descripcion = "Programador",
            foto = R.drawable.antoniolovato,
            skills = """
        💻 Skills relevantes:
        - Desarrollo en Kotlin y Android Studio
        - FastAPI y Python para APIs
        - Docker y PostgreSQL
        - Integración IoT con ESP32 y AWS IoT Core
    """.trimIndent(),
            experiencia = """
        📂 Experiencias previas:
        1. Programador en TechSchool Systems (2021-2023)
        2. Backend Developer en CloudIoT Solutions (2019-2021)
        3. Junior Developer en SoftEdu (2017-2019)
        4. Full Stack Developer en EduTech Global (2023–2024)
        5. IoT Engineer en SmartCampus Solutions (2020–2022)
        6. Software Tester en InnovApp (2016–2017)
    """.trimIndent(),
            id = 1
        )
        ,
        Perfil(
            nombre = "Sofia Catalan",
            descripcion = "Diseñadora UX/UI",
            foto = R.drawable.sofiacatalan,
            skills = """
        🎨 Skills relevantes:
        - Diseño UX/UI con Figma y Adobe XD
        - Prototipado interactivo y test de usabilidad
        - Diseño de interfaces accesibles (WCAG)
        - Branding, identidad visual y diseño editorial
        - Motion graphics básicos con After Effects
    """.trimIndent(),
            experiencia = """
        📂 Experiencias previas:
        1. UX/UI Designer en BrightApps Studio (2022–2024) – Diseño de apps móviles centradas en el usuario.
        2. Diseñadora gráfica en Creativa Agency (2020–2022) – Branding y campañas visuales.
        3. Junior Designer en PixelForge (2018–2020) – Diseño web y prototipos interactivos.
        4. Freelance Designer (2016–2018) – Identidad visual para pequeñas empresas.
    """.trimIndent(),
            id = 2
        ),
        Perfil(
            nombre = "Laura Gómez",
            descripcion = "Especialista en Marketing Digital",
            foto = R.drawable.lauragomez,
            skills = """
        📈 Skills relevantes:
        - Estrategias de marketing digital y growth hacking
        - Gestión de redes sociales y creación de contenido
        - SEO/SEM con Google Ads y Meta Ads
        - Email marketing con Mailchimp y HubSpot
        - Análisis de métricas con Google Analytics
    """.trimIndent(),
            experiencia = """
        📂 Experiencias previas:
        1. Marketing Manager en SocialBoost (2021–2024) – Campañas de crecimiento para startups.
        2. Content Strategist en MediaFlow (2019–2021) – Gestión de contenido y redes sociales.
        3. SEO Specialist en WebRank Solutions (2017–2019) – Optimización de posicionamiento orgánico.
        4. Community Manager freelance (2015–2017) – Gestión de comunidades online.
    """.trimIndent(),
            id = 3
        ),
        Perfil(
            nombre = "Barnie Rodriguez",
            descripcion = "Programador Full Stack",
            foto = R.drawable.barnierodriguez,
            skills = """
        💻 Skills relevantes:
        - Desarrollo Full Stack con Java, Spring Boot y Node.js
        - Bases de datos SQL y NoSQL (MySQL, MongoDB)
        - DevOps básico: Docker, CI/CD y GitHub Actions
        - Desarrollo de APIs REST y microservicios
        - Frontend con React y TypeScript
    """.trimIndent(),
            experiencia = """
        📂 Experiencias previas:
        1. Full Stack Developer en CodeWave (2022–2024) – Microservicios y dashboards internos.
        2. Backend Developer en DataBridge (2020–2022) – APIs de alto rendimiento con Spring Boot.
        3. Frontend Developer en PixelSoft (2018–2020) – Interfaces web con React.
        4. Junior Developer en DevStart (2016–2018) – Proyectos web para clientes pequeños.
    """.trimIndent(),
            id = 4
        )
    )
}


