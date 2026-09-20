# Campus Naksha Architecture

## Microservices

### 1. Identity Service

Responsibilities:

- User registration
- Authentication
- JWT generation
- User management
- Roles
- Account status


### 2. Incident Service

Responsibilities:

- Campus issue reporting
- Issue categories
- Issue priority
- Issue status
- Map coordinates
- Issue history


### 3. Emergency Service

Responsibilities:

- Guard profiles
- Guard availability
- Guard live locations
- SOS requests
- Nearest guard search
- Guard assignment
- Emergency state management
- Live emergency locations


### 4. Parking Service

Responsibilities:

- Parking zones
- Parking slots
- Parking availability
- Parking simulation


### 5. Notification Service

Responsibilities:

- WebSocket connections
- STOMP topics
- RabbitMQ event consumers
- Real-time frontend notifications


## Infrastructure

### API Gateway

Single entry point for frontend REST requests.


### Service Registry

Service discovery using Eureka.


### PostgreSQL

Persistent storage.


### Redis

Temporary and real-time state.

Main uses:

- Guard locations
- SOS locations
- GEO search


### RabbitMQ

Asynchronous inter-service events.


## Rule

A microservice must never directly access another microservice's database.