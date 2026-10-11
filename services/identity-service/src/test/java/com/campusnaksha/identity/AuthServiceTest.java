package com.campusnaksha.identity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.campusnaksha.identity.auth.AuthService;
import com.campusnaksha.identity.auth.JwtService;
import com.campusnaksha.identity.auth.dto.LoginRequest;
import com.campusnaksha.identity.auth.dto.LoginResponse;
import com.campusnaksha.identity.auth.dto.RegisterRequest;
import com.campusnaksha.identity.auth.dto.UserResponse;
import com.campusnaksha.identity.exception.AccountDisabledException;
import com.campusnaksha.identity.exception.EmailAlreadyExistsException;
import com.campusnaksha.identity.exception.InvalidCredentialsException;
import com.campusnaksha.identity.token.IssuedRefreshToken;
import com.campusnaksha.identity.token.RefreshTokenService;
import com.campusnaksha.identity.user.User;
import com.campusnaksha.identity.user.UserRepository;
import com.campusnaksha.identity.user.UserRole;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    private AuthService authService;

    @BeforeEach
    void setUp() {

        authService =
                new AuthService(
                        userRepository,
                        passwordEncoder,
                        jwtService,
                        refreshTokenService
                );
    }

    @Test
    void registrationAlwaysCreatesStudent() {

        RegisterRequest request =
                new RegisterRequest(
                        " Rahul Sharma ",
                        " RAHUL@CAMPUS.EDU ",
                        "StrongPassword123",
                        "9876543210"
                );

        when(
                userRepository
                        .existsByEmailIgnoreCase(
                                "rahul@campus.edu"
                        )
        ).thenReturn(false);

        when(
                passwordEncoder.encode(
                        "StrongPassword123"
                )
        ).thenReturn("hashed-password");

        when(
                userRepository.save(any(User.class))
        ).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        UserResponse response =
                authService.register(request);

        assertEquals(
                "rahul@campus.edu",
                response.email()
        );

        assertEquals(
                UserRole.STUDENT,
                response.role()
        );
    }

    @Test
    void duplicateEmailIsRejected() {

        RegisterRequest request =
                new RegisterRequest(
                        "Rahul Sharma",
                        "rahul@campus.edu",
                        "StrongPassword123",
                        null
                );

        when(
                userRepository
                        .existsByEmailIgnoreCase(
                                "rahul@campus.edu"
                        )
        ).thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () ->
                        authService.register(request)
        );
    }

    @Test
    void invalidPasswordIsRejected() {

        User user =
                user(
                        true
                );

        when(
                userRepository
                        .findByEmailIgnoreCase(
                                "rahul@campus.edu"
                        )
        ).thenReturn(Optional.of(user));

        when(
                passwordEncoder.matches(
                        "WrongPassword",
                        user.getPasswordHash()
                )
        ).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () ->
                        authService.login(
                                new LoginRequest(
                                        "rahul@campus.edu",
                                        "WrongPassword"
                                )
                        )
        );

        verify(
                refreshTokenService,
                never()
        ).create(any());
    }

    @Test
    void disabledAccountIsRejected() {

        User user =
                user(false);

        when(
                userRepository
                        .findByEmailIgnoreCase(
                                "rahul@campus.edu"
                        )
        ).thenReturn(Optional.of(user));

        when(
                passwordEncoder.matches(
                        "StrongPassword123",
                        user.getPasswordHash()
                )
        ).thenReturn(true);

        assertThrows(
                AccountDisabledException.class,
                () ->
                        authService.login(
                                new LoginRequest(
                                        "rahul@campus.edu",
                                        "StrongPassword123"
                                )
                        )
        );
    }

    @Test
    void loginCreatesAccessAndRefreshTokens() {

        User user =
                user(true);

        when(
                userRepository
                        .findByEmailIgnoreCase(
                                "rahul@campus.edu"
                        )
        ).thenReturn(Optional.of(user));

        when(
                passwordEncoder.matches(
                        "StrongPassword123",
                        user.getPasswordHash()
                )
        ).thenReturn(true);

        when(
                jwtService.generateAccessToken(user)
        ).thenReturn("access-token");

        when(
                jwtService.getAccessTokenExpiresInSeconds()
        ).thenReturn(900L);

        when(
                refreshTokenService.create(user)
        ).thenReturn(
                new IssuedRefreshToken(
                        "refresh-token",
                        604800L
                )
        );

        LoginResponse response =
                authService.login(
                        new LoginRequest(
                                "rahul@campus.edu",
                                "StrongPassword123"
                        )
                );

        assertEquals(
                "access-token",
                response.accessToken()
        );

        assertEquals(
                "refresh-token",
                response.refreshToken()
        );

        assertEquals(
                900L,
                response.expiresIn()
        );
    }

    private User user(
            boolean enabled
    ) {

        Instant now = Instant.now();

        return new User(
                UUID.randomUUID(),
                "Rahul Sharma",
                "rahul@campus.edu",
                "hashed-password",
                "9876543210",
                UserRole.STUDENT,
                enabled,
                now,
                now
        );
    }
}