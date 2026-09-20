# ADR-002: Database Per Service

## Status

Accepted


## Decision

Every business service owns its own database.


Identity Service:

identity_db


Incident Service:

incident_db


Emergency Service:

emergency_db


Parking Service:

parking_db


## Rule

One service cannot directly query or modify another service's database.


## Cross-Service References

UUIDs may be stored as logical references.

Example:

CampusIssue.reportedByUserId

references a User UUID conceptually.

It is NOT a cross-database foreign key.