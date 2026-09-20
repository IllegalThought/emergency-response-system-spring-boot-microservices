# Campus Naksha Requirements

## 1. Project Objective

Campus Naksha is a real-time campus monitoring and emergency response
platform.

The system allows:

- Students to report campus problems.
- Students to view reported issues on a campus map.
- Administrators to manage reported issues.
- Students to trigger emergency SOS requests.
- The system to automatically locate the nearest available guard.
- Guards to accept emergency requests.
- Students and guards to share live locations during emergencies.
- Students to view campus parking availability.
- Administrators to manage parking slots.
- The system to provide real-time updates using WebSockets.

The project does not use physical IoT hardware.

Parking sensor information will be simulated by the backend.


## 2. System Actors

### Student

A student can:

- Register.
- Login.
- View campus map.
- Report campus issues.
- View issue status.
- Trigger SOS.
- Share live location during an emergency.
- View assigned guard.
- View guard live location.
- Cancel SOS when allowed.
- View parking availability.


### Guard

A guard can:

- Login.
- Set availability.
- Share current location.
- Receive SOS requests.
- Accept SOS assignments.
- Reject SOS assignments.
- View student location.
- Mark emergency as responding.
- Resolve emergency.


### Administrator

An administrator can:

- Login.
- View users.
- Manage user status.
- View all campus issues.
- Change issue status.
- Change issue priority.
- Manage guards.
- View active emergencies.
- View historical emergencies.
- Manage parking zones.
- Manage parking slots.
- View analytics.


## 3. Functional Requirements

### Authentication

FR-001 Users must authenticate before accessing protected resources.

FR-002 Users must have one of the following roles:

- STUDENT
- GUARD
- ADMIN

FR-003 Authorization must be role based.


### Campus Issue Management

FR-010 Students can create campus issues.

FR-011 Issues must contain location coordinates.

FR-012 Users can view issues on the campus map.

FR-013 Administrators can update issue status.

FR-014 Users must receive real-time issue status updates.

FR-015 Issues can have priorities.


### Emergency SOS

FR-020 Students can trigger an SOS.

FR-021 SOS must contain the student's location.

FR-022 The system must identify nearby available guards.

FR-023 The system must assign an available guard.

FR-024 A guard cannot handle multiple active SOS requests simultaneously.

FR-025 Guards can accept emergency assignments.

FR-026 Students must receive assignment updates in real time.

FR-027 Student location can update while an SOS is active.

FR-028 Guard location can update while responding.

FR-029 Guards can mark emergencies as resolved.


### Parking

FR-030 Users can view parking zones.

FR-031 Users can view parking availability.

FR-032 Administrators can modify parking slots.

FR-033 The backend can simulate parking occupancy.

FR-034 Parking updates must be sent in real time.


## 4. Non-Functional Requirements

NFR-001 REST APIs must use JSON.

NFR-002 Authentication must use JWT.

NFR-003 Passwords must never be stored as plaintext.

NFR-004 The system must validate all incoming requests.

NFR-005 Service data must be isolated by microservice.

NFR-006 Live guard locations should be stored in Redis.

NFR-007 Persistent data must be stored in PostgreSQL.

NFR-008 Inter-service asynchronous events will use RabbitMQ.

NFR-009 Client real-time communication will use WebSocket/STOMP.

NFR-010 Services must support Docker deployment.

NFR-011 Database migrations must use Flyway.

NFR-012 Critical operations must be logged.

NFR-013 Concurrent SOS assignments must not assign the same guard twice.