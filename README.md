Alke Wallet

Aplicación Full Stack desarrollada con Java, Spring Boot y MySQL que simula una billetera digital, permitiendo a los usuarios autenticarse, consultar saldos, realizar depósitos, efectuar transferencias y revisar su historial de transacciones.

Descripción General

Alke Wallet es una aplicación web diseñada para demostrar la implementación de una arquitectura basada en Spring Boot utilizando una API REST conectada a una base de datos MySQL.

La aplicación incluye una interfaz web simple construida con HTML, CSS y JavaScript, servida directamente desde Spring Boot.

Tecnologías Utilizadas
Backend
Java 25
Spring Boot 4.1.0
Spring Web
Spring JDBC (JdbcTemplate)
Maven
Base de Datos
MySQL 8.x o superior
MySQL Connector/J
Frontend
HTML5
CSS3
Bootstrap
JavaScript
Fetch API
Herramientas de Desarrollo
Visual Studio Code
Maven
Git
Arquitectura del Proyecto

Actualmente el proyecto sigue una arquitectura por capas:

Cliente Web
     │
     ▼
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
MySQL
Componentes
Controller

Expone los endpoints REST consumidos por el frontend.

Service

Implementa la lógica de negocio.

Repository

Gestiona el acceso a datos mediante JdbcTemplate.

Model

Representa las entidades del dominio.

Estructura del Proyecto
alke-wallet
│
├── pom.xml
├── README.md
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.alkewallet
│   │   │       ├── controller
│   │   │       ├── model
│   │   │       ├── repository
│   │   │       ├── service
│   │   │       └── AlkeWalletApplication.java
│   │   │
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   │           ├── login.html
│   │           ├── menu.html
│   │           ├── deposit.html
│   │           ├── sendmoney.html
│   │           └── transactions.html
│   │
│   └── test
│
└── logs
Requisitos

Antes de ejecutar el proyecto asegúrese de contar con:

Java 25 o superior
Maven 3.9 o superior
MySQL 8.x o superior
Visual Studio Code (opcional)
Extensión Java Extension Pack para VS Code

Verificar instalación:

java --version
mvn --version
mysql --version
Configuración de Base de Datos
Crear Base de Datos

Ejecutar el siguiente script en MySQL:

CREATE DATABASE IF NOT EXISTS alkewallet
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE alkewallet;
Creación de Tablas
Tabla Usuarios
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL DEFAULT 0
);
Tabla Transacciones
CREATE TABLE transacciones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_origen BIGINT,
    usuario_destino BIGINT,
    monto DECIMAL(15,2) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_origen
        FOREIGN KEY(usuario_origen)
        REFERENCES usuarios(id),
    CONSTRAINT fk_destino
        FOREIGN KEY(usuario_destino)
        REFERENCES usuarios(id)
);
Datos de Prueba

IMPORTANTE: Si se implementa BCrypt en futuras versiones, las contraseñas deberán almacenarse cifradas.

INSERT INTO usuarios(nombre,email,password,saldo)
VALUES
('Administrador','admin@alkewallet.cl','admin123',1000000),
('Juan Perez','juan@alkewallet.cl','juan123',500000),
('Maria Soto','maria@alkewallet.cl','maria123',250000);
Verificación de Conexión

Comprobar usuarios registrados:

SELECT * FROM usuarios;

Consultar saldo:

SELECT nombre,saldo
FROM usuarios;

Consultar historial:

SELECT *
FROM transacciones
ORDER BY fecha DESC;
Configuración de Spring Boot

Editar:

src/main/resources/application.properties

Ejemplo:

spring.datasource.url=jdbc:mysql://localhost:3306/alkewallet
spring.datasource.username=root
spring.datasource.password=password

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

logging.file.name=logs/alke-wallet.log
Compilar el Proyecto

Desde la raíz del proyecto:

mvn clean package

Si la compilación es exitosa se generará:

target/alke-wallet-0.0.1-SNAPSHOT.jar
Ejecutar la Aplicación
Desde Maven
mvn spring-boot:run
Desde JAR
java -jar target/alke-wallet-0.0.1-SNAPSHOT.jar
Acceso a la Aplicación

Abrir en el navegador:

http://localhost:8080
Páginas Disponibles
/login.html
/menu.html
/deposit.html
/sendmoney.html
/transactions.html
API REST
Login
POST /api/login

Permite autenticar usuarios.

Consultar Saldo
GET /api/usuario/saldo

Retorna el saldo disponible.

Realizar Depósito
POST /api/deposito

Permite agregar fondos a una cuenta.

Transferir Dinero
POST /api/transferencia

Permite transferir fondos entre usuarios.

Historial de Transacciones
GET /api/transacciones

Obtiene el historial registrado.

Logs

La aplicación registra información en:

logs/alke-wallet.log

También puede visualizarse directamente desde la consola al iniciar Spring Boot.

Ejecución desde Visual Studio Code
Abrir la carpeta del proyecto.
Esperar la indexación de Maven.
Ejecutar:
mvn clean package
Iniciar:
mvn spring-boot:run

o ejecutar directamente la clase:

AlkeWalletApplication.java
Estado Actual del Proyecto

Funcionalidades implementadas:

Inicio de sesión
Consulta de saldo
Depósitos
Transferencias
Historial de transacciones
Persistencia en MySQL
Frontend integrado
Registro de logs
Mejoras Futuras
Contraseñas cifradas con BCrypt
Validaciones de entrada
DTOs para API REST
Manejo global de excepciones
Spring Security
JWT
Docker
Tests unitarios y de integración
Documentación OpenAPI / Swagger
Autor

Proyecto desarrollado como ejercicio académico para la implementación de una billetera digital utilizando Spring Boot y MySQL.