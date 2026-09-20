# Campus Naksha API Contracts


# Authentication


## Register Student

POST /api/auth/register

Request:

{
  "name": "Rahul Sharma",
  "email": "rahul@campus.edu",
  "password": "StrongPassword123",
  "phone": "9876543210"
}


Response:

{
  "id": "UUID",
  "name": "Rahul Sharma",
  "email": "rahul@campus.edu",
  "role": "STUDENT"
}


Important:

The client cannot provide the role.

Normal registration always creates STUDENT.


## Login

POST /api/auth/login

Request:

{
  "email": "rahul@campus.edu",
  "password": "StrongPassword123"
}


Response:

{
  "accessToken": "...",
  "refreshToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}


## Current User

GET /api/users/me

# Incident APIs


## Create Issue

POST /api/issues

Request:

{
  "title": "Broken street light",
  "description": "Street light near the library is not working.",
  "category": "LIGHTING",
  "latitude": 18.5204,
  "longitude": 73.8567,
  "locationName": "Library Road"
}


The backend determines:

reportedByUserId

status

priority

createdAt


Defaults:

status = REPORTED

priority = MEDIUM


## Get Issues

GET /api/issues


## Get Issue

GET /api/issues/{issueId}


## Map Issues

GET /api/issues/map


## Filter by Status

GET /api/issues?status=IN_PROGRESS


## Filter by Category

GET /api/issues?category=WATER


## Update Issue Status

PATCH /api/issues/{issueId}/status

ADMIN only.

Request:

{
  "status": "ACKNOWLEDGED"
}

# Guard APIs


## Current Guard

GET /api/guards/me


## Update Guard Status

PATCH /api/guards/me/status

Request:

{
  "status": "AVAILABLE"
}


## Update Guard Location

PUT /api/guards/me/location

Request:

{
  "latitude": 18.5204,
  "longitude": 73.8567
}
# SOS APIs


## Create SOS

POST /api/sos

Request:

{
  "latitude": 18.5204,
  "longitude": 73.8567,
  "message": "I need emergency assistance"
}


The student ID must come from authentication.

The frontend must NOT send:

studentUserId


Response:

{
  "id": "UUID",
  "status": "SEARCHING_FOR_GUARD",
  "createdAt": "2026-09-20T03:30:00Z"
}


## Get SOS

GET /api/sos/{sosId}


## Accept SOS

POST /api/sos/{sosId}/accept


## Start Responding

POST /api/sos/{sosId}/respond


## Resolve SOS

POST /api/sos/{sosId}/resolve

Request:

{
  "resolutionNote": "Student safely escorted to campus medical centre."
}


## Cancel SOS

POST /api/sos/{sosId}/cancel


## Update Student Location

PUT /api/sos/{sosId}/student-location

Request:

{
  "latitude": 18.5210,
  "longitude": 73.8571
}

# Parking APIs


## Get Parking Zones

GET /api/parking/zones


## Get Parking Zone

GET /api/parking/zones/{zoneId}


## Get Parking Slots

GET /api/parking/zones/{zoneId}/slots


## Change Parking Slot Status

PATCH /api/parking/slots/{slotId}/status

ADMIN only.

Request:

{
  "status": "OCCUPIED"
}


# Parking Simulator


## Start Simulator

POST /api/admin/parking/simulator/start


## Stop Simulator

POST /api/admin/parking/simulator/stop