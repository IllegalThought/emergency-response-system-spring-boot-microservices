# Campus Naksha Event Contracts


# Event Format

All RabbitMQ events follow the structure:

{
  "eventId": "UUID",
  "eventType": "EVENT_TYPE",
  "occurredAt": "UTC_TIMESTAMP",
  "payload": {}
}


# Routing Keys

## Incident

issue.created

issue.status.changed


## Emergency

sos.created

sos.guard.assigned

sos.accepted

sos.responding

sos.resolved

sos.cancelled


## Parking

parking.slot.updated


# Event Types

ISSUE_CREATED

ISSUE_STATUS_CHANGED

SOS_CREATED

SOS_GUARD_ASSIGNED

SOS_ACCEPTED

SOS_RESPONDING

SOS_RESOLVED

SOS_CANCELLED

PARKING_SLOT_UPDATED


# Example SOS Assigned Event

{
  "eventId": "5a590e08-a5ef-4677-94ff-e14b4f607387",

  "eventType": "SOS_GUARD_ASSIGNED",

  "occurredAt": "2026-09-20T03:30:00Z",

  "payload": {

    "sosId":
    "2fe05973-29e2-46db-a5af-5c267e9e2084",

    "studentUserId":
    "93b49930-85a3-4145-bf6b-41d1058f56db",

    "guardId":
    "6ac52660-ee39-4377-bc9a-b3ccc249ac78"
  }
}