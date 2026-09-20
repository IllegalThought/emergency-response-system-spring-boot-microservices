# ADR-001: Microservice Architecture

## Status

Accepted


## Decision

Campus Naksha will use a microservice architecture.


## Business Services

- Identity Service
- Incident Service
- Emergency Service
- Parking Service
- Notification Service


## Infrastructure

- API Gateway
- Eureka Service Registry
- PostgreSQL
- Redis
- RabbitMQ


## Reason

The system contains clearly separated business domains.

The architecture also demonstrates:

- distributed backend development
- service discovery
- API gateway
- asynchronous communication
- real-time communication
- database ownership


## Trade-Off

Microservices increase complexity compared with a monolithic application.

This is accepted because distributed systems are a core technical goal of
the project.