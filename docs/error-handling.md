# Campus Naksha Error Handling


# Standard Error Response

All microservices should return errors using a consistent format.

Example:

{
  "timestamp": "2026-09-20T03:30:00Z",

  "status": 400,

  "error": "VALIDATION_ERROR",

  "message": "Request validation failed",

  "path": "/api/issues",

  "fieldErrors": {
    "title": "Title is required"
  }
}


# Error Codes


## Identity

USER_NOT_FOUND

EMAIL_ALREADY_EXISTS

INVALID_CREDENTIALS

ACCOUNT_DISABLED


## Incident

ISSUE_NOT_FOUND

INVALID_ISSUE_STATE


## Emergency

GUARD_NOT_FOUND

GUARD_NOT_AVAILABLE

SOS_NOT_FOUND

INVALID_SOS_STATE

NO_GUARD_AVAILABLE


## Parking

PARKING_ZONE_NOT_FOUND

PARKING_SLOT_NOT_FOUND


## Common

VALIDATION_ERROR

ACCESS_DENIED

INTERNAL_SERVER_ERROR


# HTTP Status Mapping


200 OK

Successful GET or update.


201 CREATED

Resource successfully created.


204 NO CONTENT

Resource successfully deleted when no body is required.


400 BAD REQUEST

Invalid input.


401 UNAUTHORIZED

Missing or invalid authentication.


403 FORBIDDEN

Authenticated but not authorized.


404 NOT FOUND

Resource does not exist.


409 CONFLICT

Business-state conflict.

Examples:

Duplicate email

Invalid SOS transition

Guard already assigned

Invalid issue transition


500 INTERNAL SERVER ERROR

Unexpected server failure.