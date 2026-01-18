# Kollab – Conecta con otros programadores

Kollab es una aplicación para **conocer y chatear con otros programadores**. Los usuarios pueden hacer login, ver perfiles uno a uno, aceptar o descartar con *swipe*, y chatear si hay coincidencia mutua. También pueden ver y editar su perfil, y aplicar filtros por género o talento.

## Funcionalidades principales

- Mostrar perfiles uno a uno con **swipe** para aceptar o descartar.
- Crear chats automáticamente si hay coincidencia.
- Editar y ver perfil.
- Filtrar perfiles por **género** o **puesto/talento**.

## Adapter y ViewHolder

**ChatAdapter**  
- Gestiona la lista de chats.  
- Infla cada chat con `item_chat.xml`.  
- Muestra foto, nombre y última frase.  
- Permite actualizar la lista con `actualizarLista()`.

**ChatViewHolder**  
- Mantiene referencias a los elementos de cada chat:  
  - `foto` → Imagen del usuario  
  - `nombre` → Nombre del usuario  
  - `ultimaFrase` → Último mensaje  
- Permite que el Adapter rellene los datos eficientemente.  

## Filtros

- Variables:  
  - `filtroGenero` → Género seleccionado  
  - `filtroPuestos` → Lista de talentos seleccionados  
- `aplicarFiltros()` filtra `PerfilData.perfiles` según estos criterios.  
- Interfaz:  
  - Género → selección única (Masculino/Femenino)  
  - Puesto → selección múltiple (Programador, UX/UI Designer, Marketing Manager)  

## Dinámica de la app

1. Login de usuario.  
2. Mostrar perfiles con swipe.  
3. Si hay coincidencia, abrir chat automáticamente.  
4. Aplicar filtros para ver perfiles específicos.  

## Tecnologías

- Lenguaje: **Kotlin**  
- UI: Android XML  
- Listas: `RecyclerView` con `Adapter` y `ViewHolder`

https://github.com/user-attachments/assets/5944e9de-5cdb-4e14-99d8-e91774ad5f0a
