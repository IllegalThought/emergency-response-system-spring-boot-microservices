# Campus Naksha Event Contracts

## 1. Purpose

Campus Naksha uses RabbitMQ for asynchronous communication between backend services.

Business services publish domain events.

The Notification Service consumes relevant events and forwards real-time updates to frontend clients using WebSocket/STOMP.


---

# 2. Standard Event Structure

All RabbitMQ events should follow a consistent envelope.

Example:

```json
{
  "eventId": "5a590e08-a5ef-4677-94ff-e14b4f607387",
  "eventType": "SOS_GUARD_ASSIGNED",
  "occurredAt": "2026-09-20T05:00:00Z",
  "source": "emergency-service",
  "payload": {}
}