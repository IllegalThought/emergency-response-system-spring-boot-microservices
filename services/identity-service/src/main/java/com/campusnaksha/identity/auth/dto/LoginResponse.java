package com.campusnaksha.identity.auth.dto;

public record LoginResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn,

        long refreshExpiresIn

) {
}