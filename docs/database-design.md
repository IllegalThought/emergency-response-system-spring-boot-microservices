# Campus Naksha Database Design

## Database Ownership

Campus Naksha follows the Database-per-Service pattern.

Each microservice owns its own persistent data.

## Databases

### Identity Service

Database:

identity_db

Responsible for:

- Users
- Authentication information
- Roles
- Refresh tokens


### Incident Service

Database:

incident_db

Responsible for:

- Campus issues
- Issue status
- Issue categories
- Issue priority
- Issue history


### Emergency Service

Database:

emergency_db

Responsible for:

- Guard profiles
- Guard status
- SOS requests
- Guard assignment
- Emergency lifecycle


### Parking Service

Database:

parking_db

Responsible for:

- Parking zones
- Parking slots
- Parking status


## Redis

Redis is not the primary database.

Redis will be used for:

- Guard live locations
- Guard GEO search
- SOS live locations
- Temporary emergency state
- Short-lived caching


## Important Microservice Rule

A service must never directly access another service's database.

Example:

Incident Service may store:

reportedByUserId

But it must NOT create a database foreign key to the Identity Service
database.

Example:

CampusIssue

id = UUID

reportedByUserId = UUID

title = String

...

reportedByUserId represents the Identity Service user's UUID.

There is no cross-database foreign key.


## Example

Identity Service:

User ID:

550e8400-e29b-41d4-a716-446655440000


Incident Service:

Issue:

id:
8137bb3a-c20e-438c-b6bb-e5c6a262ce89

reportedByUserId:
550e8400-e29b-41d4-a716-446655440000


The Incident Service stores only the UUID.

It does not directly read the Identity Service database.


## Communication Between Services

Services communicate using:

1. REST APIs

or

2. RabbitMQ events

## ID Strategy

All primary business entities use UUID identifiers.

Examples:

User.id

CampusIssue.id

Guard.id

SOSRequest.id

ParkingZone.id

ParkingSlot.id

UUIDs allow each microservice to independently generate identifiers.
They never communicate by directly reading another service's database.