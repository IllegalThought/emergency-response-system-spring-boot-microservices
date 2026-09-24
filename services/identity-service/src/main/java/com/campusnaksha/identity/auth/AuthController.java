package com.campusnaksha.identity.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campusnaksha.identity.auth.dto.LoginRequest;
import com.campusnaksha.identity.auth.dto.LoginResponse;
import com.campusnaksha.identity.auth.dto.LogoutRequest;
import com.campusnaksha.identity.auth.dto.RefreshTokenRequest;
import com.campusnaksha.identity.auth.dto.RegisterRequest;
import com.campusnaksha.identity.auth.dto.UserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        UserResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/login")
public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request
) {

    return ResponseEntity.ok(
            authService.login(request)
    );
}
@PostMapping("/refresh")
public ResponseEntity<LoginResponse> refresh(
        @Valid @RequestBody RefreshTokenRequest request
) {

    return ResponseEntity.ok(
            authService.refresh(request)
    );
}
@PostMapping("/logout")
public ResponseEntity<Void> logout(
        @Valid @RequestBody LogoutRequest request
) {

    authService.logout(
            request.refreshToken()
    );

    return ResponseEntity.noContent().build();
}
}