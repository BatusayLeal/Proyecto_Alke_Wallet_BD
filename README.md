# Alke Wallet

Aplicacion fullstack de billetera digital con Java, Spring Boot y MySQL.

Permite autenticar usuarios, consultar saldo, depositar, transferir dinero y revisar el historial de transacciones. El frontend (HTML, CSS, JavaScript y Bootstrap) se sirve desde Spring Boot y consume una API REST persistida en MySQL.

**Stack:** Java 25 · Spring Boot 4.1.0 · Spring Web · JdbcTemplate · MySQL 8 · Maven · HTML5 / CSS3 / Bootstrap / Fetch API

---

## Tabla de contenidos

1. [Funcionalidades](#funcionalidades)
2. [Arquitectura](#arquitectura)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Requisitos](#requisitos)
5. [Base de datos](#base-de-datos)
6. [Configuracion de Spring Boot](#configuracion-de-spring-boot)
7. [Compilar y ejecutar](#compilar-y-ejecutar)
8. [Pantallas](#pantallas)
9. [API REST](#api-rest)
10. [Logs](#logs)
11. [Mejoras futuras](#mejoras-futuras)

---

## Funcionalidades

- Inicio de sesion
- Consulta de saldo
- Depositos
- Transferencias entre usuarios
- Historial de transacciones
- Persistencia en MySQL
- Frontend integrado en `src/main/resources/static`
- Registro de logs en archivo y consola

---

## Arquitectura

Arquitectura por capas:

```text
Cliente Web (HTML / JS)
        |
        v
   Controller   ->  endpoints REST
        |
        v
    Service     ->  logica de negocio
        |
        v
  Repository    ->  JdbcTemplate
        |
        v
     MySQL
```

| Capa | Responsabilidad |
| :--- | :--- |
| Controller | Expone los endpoints REST que consume el frontend |
| Service | Implementa la logica de negocio |
| Repository | Acceso a datos con JdbcTemplate |
| Model | Entidades del dominio |

---

## Estructura del proyecto

```text
alke-wallet
|
|-- pom.xml
|-- README.md
|
|-- src
|   |-- main
|   |   |-- java
|   |   |   `-- com.alkewallet
|   |   |       |-- controller
|   |   |       |-- model
|   |   |       |-- repository
|   |   |       |-- service
|   |   |       `-- AlkeWalletApplication.java
|   |   |
|   |   `-- resources
|   |       |-- application.properties
|   |       `-- static
|   |           |-- login.html
|   |           |-- menu.html
|   |           |-- deposit.html
|   |           |-- sendmoney.html
|   |           `-- transactions.html
|   |
|   `-- test
|
`-- logs
```

---

## Requisitos

- Java 25 o superior
- Maven 3.9 o superior
- MySQL 8.x o superior
- VS Code + Java Extension Pack (opcional)

Verificar instalacion:

```bash
java --version
mvn --version
mysql --version
```

---

## Base de datos

### 1. Crear base de datos

```sql
CREATE DATABASE IF NOT EXISTS alkewallet
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE alkewallet;
```

### 2. Tabla usuarios

```sql
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL DEFAULT 0
);
```

### 3. Tabla transacciones

```sql
CREATE TABLE transacciones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_origen BIGINT,
    usuario_destino BIGINT,
    monto DECIMAL(15,2) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_origen
        FOREIGN KEY (usuario_origen) REFERENCES usuarios(id),
    CONSTRAINT fk_destino
        FOREIGN KEY (usuario_destino) REFERENCES usuarios(id)
);
```

### 4. Datos de prueba

Nota: las claves van en texto plano. Si se agrega BCrypt, deben almacenarse cifradas.

```sql
INSERT INTO usuarios (nombre, email, password, saldo)
VALUES
('Administrador', 'admin@alkewallet.cl', 'admin123', 1000000),
('Juan Perez', 'juan@alkewallet.cl', 'juan123', 500000),
('Maria Soto', 'maria@alkewallet.cl', 'maria123', 250000);
```

### 5. Consultas de verificacion

```sql
SELECT * FROM usuarios;

SELECT nombre, saldo FROM usuarios;

SELECT * FROM transacciones ORDER BY fecha DESC;
```

---

## Configuracion de Spring Boot

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/alkewallet
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

logging.file.name=logs/alke-wallet.log
```

---

## Compilar y ejecutar

Desde la raiz del proyecto:

```bash
mvn clean package
```

Artefacto generado:

```text
target/alke-wallet-0.0.1-SNAPSHOT.jar
```

Ejecutar con Maven:

```bash
mvn spring-boot:run
```

O con el JAR:

```bash
java -jar target/alke-wallet-0.0.1-SNAPSHOT.jar
```

En VS Code: abrir la carpeta, esperar la indexacion de Maven y ejecutar `AlkeWalletApplication.java`, o usar los comandos anteriores.

Aplicacion:

```text
http://localhost:8080
```

---

## Pantallas

| Ruta | Descripcion |
| :--- | :--- |
| `/login.html` | Inicio de sesion |
| `/menu.html` | Menu principal |
| `/deposit.html` | Depositos |
| `/sendmoney.html` | Transferencias |
| `/transactions.html` | Historial |

---

## API REST

| Metodo | Endpoint | Descripcion |
| :--- | :--- | :--- |
| POST | `/api/login` | Autenticar usuario |
| GET | `/api/usuario/saldo` | Consultar saldo |
| POST | `/api/deposito` | Agregar fondos |
| POST | `/api/transferencia` | Transferir entre usuarios |
| GET | `/api/transacciones` | Historial de transacciones |

---

## Logs

Archivo:

```text
logs/alke-wallet.log
```

Tambien se muestran en la consola al iniciar Spring Boot.

---

## Mejoras futuras

- Cifrado de contrasenas con BCrypt
- Validaciones de entrada
- DTOs para la API REST
- Manejo global de excepciones
- Spring Security
- JWT
- Docker
- Tests unitarios y de integracion
- Documentacion OpenAPI / Swagger

---

## Autor

Proyecto academico de billetera digital con Spring Boot y MySQL.

Cristian Leal · [GitHub](https://github.com/BatusayLeal)
