package com.campusnaksha.identity.auth.dto;

public record LoginResponse(

        String accessToken,

        String tokenType,

        long expiresIn

) {
}