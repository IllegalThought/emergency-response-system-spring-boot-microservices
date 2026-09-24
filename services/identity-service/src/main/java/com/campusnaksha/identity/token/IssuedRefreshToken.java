package com.campusnaksha.identity.token;

public record IssuedRefreshToken(

        String token,

        long expiresIn

) {
}