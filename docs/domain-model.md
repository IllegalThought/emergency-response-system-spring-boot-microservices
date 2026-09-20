# Campus Naksha Domain Model

Campus Naksha contains five main backend domains:

1. Identity
2. Incident
3. Emergency
4. Parking
5. Notification


# Identity Domain

## User

Fields:

- id : UUID
- name : String
- email : String
- passwordHash : String
- phone : String
- role : UserRole
- enabled : boolean
- createdAt : Instant
- updatedAt : Instant


## UserRole

Values:

- STUDENT
- GUARD
- ADMIN

## RefreshToken

Fields:

- id : UUID
- userId : UUID
- tokenHash : String
- expiresAt : Instant
- revokedAt : Instant
- createdAt : Instant


# Incident Domain

## IssueStatusHistory

Fields:

- id : UUID
- issueId : UUID
- previousStatus : IssueStatus
- newStatus : IssueStatus
- changedByUserId : UUID
- changedAt : Instant
- note : String

## CampusIssue

Fields:

- id : UUID
- reportedByUserId : UUID
- title : String
- description : String
- category : IssueCategory
- status : IssueStatus
- priority : IssuePriority
- latitude : BigDecimal
- longitude : BigDecimal
- locationName : String
- createdAt : Instant
- updatedAt : Instant
- resolvedAt : Instant


## IssueCategory

Values:

- INFRASTRUCTURE
- ELECTRICAL
- WATER
- SANITATION
- ROAD_DAMAGE
- SECURITY
- LIGHTING
- OTHER


## IssueStatus

Values:

- REPORTED
- ACKNOWLEDGED
- IN_PROGRESS
- RESOLVED
- REJECTED


## IssuePriority

Values:

- LOW
- MEDIUM
- HIGH
- CRITICAL


# Emergency Domain

## SosAssignment

Fields:

- id : UUID
- sosId : UUID
- guardId : UUID
- status : AssignmentStatus
- distanceMeters : BigDecimal
- offeredAt : Instant
- respondedAt : Instant


## AssignmentStatus

Values:

- PENDING
- ACCEPTED
- REJECTED
- EXPIRED
- CANCELLED
## Guard

Fields:

- id : UUID
- userId : UUID
- employeeNumber : String
- status : GuardStatus
- lastLocationUpdate : Instant
- createdAt : Instant
- updatedAt : Instant


## GuardStatus

Values:

- AVAILABLE
- RESERVED
- BUSY
- OFF_DUTY
- OFFLINE


## SOSRequest

Fields:

- id : UUID
- studentUserId : UUID
- assignedGuardId : UUID
- message : String
- latitude : BigDecimal
- longitude : BigDecimal
- status : SosStatus
- createdAt : Instant
- assignedAt : Instant
- acceptedAt : Instant
- respondingAt : Instant
- resolvedAt : Instant
- cancelledAt : Instant
- resolutionNote : String


## SosStatus

Values:

- CREATED
- SEARCHING_FOR_GUARD
- GUARD_ASSIGNED
- ACCEPTED
- RESPONDING
- RESOLVED
- CANCELLED
- NO_GUARD_AVAILABLE


# Parking Domain

## ParkingZone

Fields:

- id : UUID
- name : String
- description : String
- latitude : BigDecimal
- longitude : BigDecimal
- capacity : Integer
- active : boolean
- createdAt : Instant
- updatedAt : Instant


## ParkingSlot

Fields:

- id : UUID
- zoneId : UUID
- slotNumber : String
- status : ParkingSlotStatus
- updatedAt : Instant


## ParkingSlotStatus

Values:

- AVAILABLE
- OCCUPIED
- RESERVED
- OUT_OF_SERVICE


# Notification Domain

The Notification Service initially does not own major business entities.

It consumes RabbitMQ events and forwards real-time updates through
WebSocket/STOMP.