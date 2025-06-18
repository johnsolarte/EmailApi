# Email Service API

Una API REST para el envío de emails con registro de historial, construida con Spring Boot.

## 🚀 Características

- ✉️ Envío de emails individuales o masivos
- 📝 Soporte para contenido HTML y texto plano
- 📊 Registro completo de historial de emails enviados
- 🔍 Filtrado de historial por estado y fechas
- 🐳 Containerización con Docker
- 💾 Base de datos H2 en memoria para desarrollo
- 🔧 Configuración flexible para diferentes entornos

## 🛠️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.3.0**
- **Spring Boot Starter Mail**
- **Spring Data JPA**
- **H2 Database** (desarrollo)
- **Oracle JDBC** (producción)
- **Lombok**
- **Maven**
- **Docker**

## 📋 Prerrequisitos

- Java 21 o superior
- Maven 3.6+
- Docker (opcional)
- Cuenta de Gmail con contraseña de aplicación (para SMTP)

## ⚙️ Configuración

### Configuración de Email

El proyecto está configurado para usar Gmail SMTP. Actualiza las siguientes propiedades en `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-contraseña-de-aplicacion
```

> **Nota**: Para Gmail, necesitas generar una contraseña de aplicación en lugar de usar tu contraseña normal.

### Variables de Entorno

Para producción, puedes usar las siguientes variables de entorno:

- `PORT`: Puerto del servidor (default: 8080)
- `SPRING_DATASOURCE_URL`: URL de la base de datos
- `SPRING_DATASOURCE_USERNAME`: Usuario de la base de datos
- `SPRING_DATASOURCE_PASSWORD`: Contraseña de la base de datos
- `DDL_AUTO`: Estrategia de DDL de Hibernate (default: create-drop)

## 🚀 Instalación y Ejecución

### Ejecución Local

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd emailservice

# Compilar y ejecutar
mvn clean install
mvn spring-boot:run
```

### Ejecución con Docker

```bash
# Construir imagen
docker build -t email-service .

# Ejecutar contenedor
docker run -p 8080:8080 email-service
```

La aplicación estará disponible en `http://localhost:8080`

## 📚 API Endpoints

### Enviar Email

**POST** `/emails/send`

Envía uno o múltiples emails.

**Request Body:**
```json
{
  "to": ["destinatario1@example.com", "destinatario2@example.com"],
  "subject": "Asunto del email",
  "body": "Contenido del email",
  "isHtml": false
}
```

**Response:**
```
Email(s) sent
```

### Obtener Historial

**GET** `/emails/history`

Obtiene el historial de emails enviados con filtros opcionales.

**Query Parameters:**
- `status` (opcional): Filtrar por estado (`SUCCESS` o `FAILED`)
- `fromDate` (opcional): Fecha desde (formato: YYYY-MM-DD)
- `toDate` (opcional): Fecha hasta (formato: YYYY-MM-DD)

**Ejemplos:**
```bash
# Todos los emails
GET /emails/history

# Solo emails exitosos
GET /emails/history?status=SUCCESS

# Emails en un rango de fechas
GET /emails/history?fromDate=2024-01-01&toDate=2024-12-31

# Combinando filtros
GET /emails/history?status=FAILED&fromDate=2024-06-01
```

**Response:**
```json
[
  {
    "id": 1,
    "recipient": "destinatario@example.com",
    "subject": "Asunto del email",
    "body": "Contenido del email",
    "sentAt": "2024-06-18T10:30:00",
    "status": "SUCCESS",
    "errorMessage": null
  }
]
```

## 🗃️ Base de Datos

### H2 Console (Desarrollo)

La consola H2 está habilitada para desarrollo y se puede acceder en:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:emaildb`
- Usuario: `sa`
- Contraseña: (vacía)

### Esquema de Base de Datos

**Tabla: email_log**
- `id` (BIGINT, PK, AUTO_INCREMENT)
- `recipient` (VARCHAR)
- `subject` (VARCHAR)
- `body` (TEXT)
- `sent_at` (TIMESTAMP)
- `status` (VARCHAR) - 'SUCCESS' o 'FAILED'
- `error_message` (VARCHAR)

## 🧪 Ejemplo de Uso

### Enviar un email simple

```bash
curl -X POST http://localhost:8080/emails/send \
  -H "Content-Type: application/json" \
  -d '{
    "to": ["test@example.com"],
    "subject": "Email de prueba",
    "body": "Este es un email de prueba",
    "isHtml": false
  }'
```

### Enviar email HTML a múltiples destinatarios

```bash
curl -X POST http://localhost:8080/emails/send \
  -H "Content-Type: application/json" \
  -d '{
    "to": ["user1@example.com", "user2@example.com"],
    "subject": "Newsletter",
    "body": "<h1>¡Hola!</h1><p>Este es un email en <strong>HTML</strong></p>",
    "isHtml": true
  }'
```

### Consultar historial

```bash
# Ver todos los emails
curl http://localhost:8080/emails/history

# Ver solo emails fallidos
curl "http://localhost:8080/emails/history?status=FAILED"
```

## 🔧 Configuración para Producción

Para producción, considera:

1. **Base de datos persistente**: Configura una base de datos como PostgreSQL o MySQL
2. **Seguridad**: Implementa autenticación y autorización
3. **Logging**: Configura logs apropiados
4. **Monitoreo**: Añade métricas y health checks
5. **Variables de entorno**: Usa variables de entorno para configuración sensible

Ejemplo de configuración para PostgreSQL:
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── EmailServiceApplication.java
│   │   ├── controller/
│   │   │   └── EmailController.java
│   │   ├── dto/
│   │   │   └── EmailRequest.java
│   │   ├── model/
│   │   │   └── EmailLog.java
│   │   ├── Repository/
│   │   │   └── EmailLogRepository.java
│   │   └── service/
│   │       └── EmailService.java
│   └── resources/
│       └── application.properties
├── Dockerfile
├── docker-compose.yaml
└── pom.xml
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 📞 Soporte

Si tienes problemas o preguntas, puedes:
- Abrir un issue en GitHub
- Contactar al equipo de desarrollo

---

**Nota**: Recuerda no compartir credenciales reales en el código fuente. Usa variables de entorno o un sistema de gestión de secretos para información sensible.
