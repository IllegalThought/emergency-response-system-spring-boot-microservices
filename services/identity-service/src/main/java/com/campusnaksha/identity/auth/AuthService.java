package com.campusnaksha.identity.auth;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusnaksha.identity.auth.dto.LoginRequest;
import com.campusnaksha.identity.auth.dto.LoginResponse;
import com.campusnaksha.identity.auth.dto.RefreshTokenRequest;
import com.campusnaksha.identity.auth.dto.RegisterRequest;
import com.campusnaksha.identity.auth.dto.UserResponse;
import com.campusnaksha.identity.exception.AccountDisabledException;
import com.campusnaksha.identity.exception.EmailAlreadyExistsException;
import com.campusnaksha.identity.exception.InvalidCredentialsException;
import com.campusnaksha.identity.token.IssuedRefreshToken;
import com.campusnaksha.identity.token.RefreshToken;
import com.campusnaksha.identity.token.RefreshTokenService;
import com.campusnaksha.identity.user.User;
import com.campusnaksha.identity.user.UserRepository;
import com.campusnaksha.identity.user.UserRole;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }


    @Transactional
    public UserResponse register(RegisterRequest request) {

        String normalizedEmail = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);


        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new EmailAlreadyExistsException(normalizedEmail);
        }


        Instant now = Instant.now();


        User user = new User(
                UUID.randomUUID(),
                request.name().trim(),
                normalizedEmail,
                passwordEncoder.encode(request.password()),
                normalizePhone(request.phone()),
                UserRole.STUDENT,
                true,
                now,
                now
        );


        User savedUser = userRepository.save(user);


        return toResponse(savedUser);
    }



    /*
     * IMPORTANT:
     * This is NOT readOnly because refresh token creation
     * writes data into refresh_tokens table.
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {


        String normalizedEmail = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);



        User user = userRepository
                .findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(
                        InvalidCredentialsException::new
                );



        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {

            throw new InvalidCredentialsException();
        }



        if (!user.isEnabled()) {

            throw new AccountDisabledException();
        }



        String accessToken =
                jwtService.generateAccessToken(user);



        IssuedRefreshToken refreshToken =
                refreshTokenService.create(user);



        return new LoginResponse(

                accessToken,

                refreshToken.token(),

                "Bearer",

                jwtService.getAccessTokenExpiresInSeconds(),

                refreshToken.expiresIn()
        );
    }





    @Transactional
    public LoginResponse refresh(
            RefreshTokenRequest request
    ) {


        RefreshToken existingToken =
                refreshTokenService.validate(
                        request.refreshToken()
                );


        User user = existingToken.getUser();



        IssuedRefreshToken rotatedToken =
                refreshTokenService.rotate(
                        request.refreshToken()
                );



        String accessToken =
                jwtService.generateAccessToken(user);



        return new LoginResponse(

                accessToken,

                rotatedToken.token(),

                "Bearer",

                jwtService.getAccessTokenExpiresInSeconds(),

                rotatedToken.expiresIn()
        );
    }





    @Transactional
    public void logout(String refreshToken) {

        refreshTokenService.revoke(refreshToken);

    }





    private String normalizePhone(String phone) {

        if (phone == null || phone.isBlank()) {
            return null;
        }

        return phone.trim();
    }





    private UserResponse toResponse(User user) {

        return new UserResponse(

                user.getId(),

                user.getName(),

                user.getEmail(),

                user.getPhone(),

                user.getRole(),

                user.isEnabled(),

                user.getCreatedAt()
        );
    }
}