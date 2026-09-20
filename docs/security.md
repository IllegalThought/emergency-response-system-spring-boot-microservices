# Campus Naksha Security


## Roles

STUDENT

GUARD

ADMIN


## Authentication

JWT authentication will be used.

Access tokens are sent using:

Authorization: Bearer <token>


# Authorization Matrix


| Operation | STUDENT | GUARD | ADMIN |
|-----------|---------|-------|-------|
| Register | YES | NO | NO |
| Login | YES | YES | YES |
| View Current User | YES | YES | YES |
| Create Issue | YES | NO | YES |
| View Issues | YES | YES | YES |
| Update Issue Status | NO | NO | YES |
| Trigger SOS | YES | NO | NO |
| View Assigned SOS | NO | YES | YES |
| Accept SOS | NO | YES | NO |
| Respond to SOS | NO | YES | NO |
| Resolve SOS | NO | YES | YES |
| Update Guard Status | NO | YES | NO |
| Update Guard Location | NO | YES | NO |
| View Parking | YES | YES | YES |
| Modify Parking | NO | NO | YES |
| Start Parking Simulator | NO | NO | YES |
| Manage Users | NO | NO | YES |


# Important Security Rules

1. The user ID must be obtained from the authenticated JWT.

2. The frontend must never be trusted to provide the logged-in user's ID.

3. The frontend must never be trusted to provide authorization roles.

4. Passwords must be stored using BCrypt.

5. Passwords must never appear in API responses.

6. Admin endpoints must require the ADMIN role.

7. Guard endpoints must require the GUARD role.