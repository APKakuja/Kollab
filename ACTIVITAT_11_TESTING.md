# Activitat 11 - Testing

## Pantalla en la que se ha probado
Registro de usuario (RegisterActivity / activity_register.xml)

## Objetivo
Validar la lógica de registro.

## Casos de prueba

| Nombre de la prueba | Tipo | Parámetros de entrada | Resultado esperado |
|---|---|---|---|
| Registro válido | Unitaria | nombre válido, apellido válido, email válido, contraseña válida, confirmación igual | `isValid = true` y sin errores |
| Email inválido | Unitaria | email sin `@` o sin formato válido | `emailError = "Email no es válido"` |
| Contraseña corta | Unitaria | contraseña con menos de 6 caracteres | `passwordError = "La contraseña debe tener mínimo 6 caracteres"` |
| Contraseñas distintas | Unitaria | contraseña y confirmación diferentes | `confirmPasswordError = "Las contraseñas no coinciden"` |
| Nombre con números | Unitaria | nombre con caracteres no válidos | `nameError = "El nombre solo puede contener letras"` |
| Error visual de email | UI Espresso | rellenar formulario con email inválido y pulsar registrar | El `EditText` de email muestra el error correspondiente |
| Error visual de confirmación | UI Espresso | poner contraseñas distintas y pulsar registrar | El campo de confirmación muestra el error correspondiente |
| Registro correcto | UI Espresso | todos los campos válidos | La `RegisterActivity` se cierra tras registrar correctamente |


