package com.campusnaksha.identity.auth.dto;

import java.time.Instant;
import java.util.UUID;

import com.campusnaksha.identity.user.UserRole;

public record UserResponse(

        UUID id,

        String name,

        String email,

        String phone,

        UserRole role,

        boolean enabled,

        Instant createdAt
) {
}