# Spring Boot Boilerplate

A template project for building Java web applications using Spring Boot 3. It showcases JWT-based authentication, WebSocket messaging, Redis integration and modular feature design.

## Features
- **JWT security** with token blacklist stored in Redis
- **REST APIs** for authentication, users, notifications, devices and more
- **WebSocket** configuration for real-time chat and notifications
- **SQL Server** persistence via Spring Data JPA
- **OpenAPI/Swagger UI** documentation via springdoc-openapi

## Requirements
- [Java 21](https://adoptium.net/)
- [Maven](https://maven.apache.org/) 3.9+
- [SQL Server](https://www.microsoft.com/en-us/sql-server) instance
- [Redis](https://redis.io/) instance

## Getting Started
1. Clone the repository:
   ```bash
   git clone <repo-url>
   cd springboot-boilerplate
   ```
2. Configure the database, Redis and JWT settings in `src/main/resources/application.properties`.
3. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   The API will start on `http://localhost:8080`.

## API Documentation
When the application is running, Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

## Running Tests
Execute all unit tests with:
```bash
./mvnw test
```

## Project Structure
```
src/
 └── main
     ├── java/com/instar/...   # Java source files
     └── resources/            # application.properties, templates
uploads/                      # uploaded files
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is not licensed.

