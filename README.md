# Airtime Management

This is a Spring Boot application for managing airtime.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* Java 17
* Maven
* Docker
* Docker Compose

### Installing

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/airtimemanagement.git
   ```
2. **Navigate to the project directory**
   ```bash
   cd airtimemanagement
   ```
3. **Start the database**
   ```bash
   docker-compose up -d
   ```
4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will be running on `http://localhost:8080`.

## Creating a User

To create a user, you can use the following cURL command:

```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "username": "user",
  "password": "password"
}' http://localhost:8080/users
```

## Logging In

To log in, you can use the following cURL command:

```bash
curl -u user:password http://localhost:8080/login
```

## API Documentation

The API documentation is available through Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Liquibase](https://www.liquibase.org/) - Database schema management
* [MariaDB](https://mariadb.org/) - Database
* [Swagger](https://swagger.io/) - API documentation
