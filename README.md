# Campus Naksha

Campus Naksha is a real-time campus monitoring and emergency response system.

The application allows students to report campus issues, trigger emergency
SOS requests, track security guards in real time, and view live campus
parking availability.

The backend uses Spring Boot microservices.


## Main Features

- User authentication and role-based authorization
- Interactive campus issue map
- Campus issue reporting
- Real-time issue updates
- Emergency SOS
- Nearest available guard discovery
- Live student and guard location tracking
- Parking availability
- Software-based parking simulation
- Real-time WebSocket notifications
- Admin dashboard


## Architecture

Backend:

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Cloud Gateway
- Eureka
- Redis
- RabbitMQ
- WebSocket/STOMP
- PostgreSQL
- Flyway
- Docker


Frontend:

- Next.js
- TypeScript
- Tailwind CSS
- Leaflet
- OpenStreetMap


## Backend Services

### Identity Service

Authentication, users and roles.


### Incident Service

Campus issue reporting and issue management.


### Emergency Service

SOS handling, guard management, geospatial search and live emergency
tracking.


### Parking Service

Parking zones, slots and parking simulation.


### Notification Service

RabbitMQ consumers and WebSocket notifications.


## Infrastructure

### API Gateway

Single backend entry point.


### Eureka

Service discovery.


### PostgreSQL

Persistent relational storage.


### Redis

Live location and geospatial search.


### RabbitMQ

Asynchronous service-to-service events.


## Project Structure

campus-naksha/

    services/

        identity-service/

        incident-service/

        emergency-service/

        parking-service/

        notification-service/

    infrastructure/

        api-gateway/

        service-registry/

    frontend/

    docs/

    docker/


## Project Status

## Current Implementation Status

### Implemented

#### Identity Service

- Student registration
- BCrypt password hashing
- Login
- JWT access tokens
- Role-based authorization
- Refresh token rotation
- Refresh token revocation
- Logout
- Account enabled/disabled validation

#### Incident Service

- Incident creation
- Incident retrieval
- Status filtering
- Category filtering
- Campus map data
- Incident status transitions
- Status history
- Priority management
- JWT-based reporter identity
- Admin-only status and priority updates
- Optimistic locking
- Validation
- Standard API errors

#### Infrastructure

- PostgreSQL
- Flyway
- Eureka Service Registry
- API Gateway
- Redis infrastructure
- RabbitMQ infrastructure

### Planned

- Emergency Service
- Guard management
- Redis GEO guard discovery
- SOS assignment
- Parking Service
- Parking simulation
- Notification Service
- RabbitMQ event integration
- WebSocket/STOMP
- Next.js frontend