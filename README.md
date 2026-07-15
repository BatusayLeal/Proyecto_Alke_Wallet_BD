# Alke Wallet - Full Stack (Spring Boot + Frontend)

Proyecto Full Stack del Alke Wallet.

## Estructura

- **Backend**: Spring Boot 3 + JDBC
- **Frontend**: HTML + Bootstrap + JavaScript (estático)
- Comunicación: `fetch()` desde el frontend hacia la API REST

## Cómo ejecutar

1. Abre el proyecto en VS Code o IntelliJ
2. Ejecuta `AlkeWalletApplication.java`
3. El backend correrá en: `http://localhost:8080`
4. Copia tu frontend dentro de `src/main/resources/static/`
5. Abre `http://localhost:8080/login.html` en el navegador

## Endpoints disponibles

- `POST /api/login` → Iniciar sesión

## Próximos pasos recomendados

- Agregar endpoint para obtener saldo
- Agregar endpoint para realizar depósitos
- Agregar endpoint para transferencias
- Modificar el frontend para usar `fetch()` en lugar de localStorage

---
Desarrollado como proyecto de aprendizaje (Trainee / Junior)