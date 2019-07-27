package com.example.biblio.service;

import com.example.biblio.exception.UserNotFoundException;
import com.example.biblio.model.User;
import com.example.biblio.model.token.RefreshToken;
import com.example.biblio.payload.JwtAuthenticationResponse;
import com.example.biblio.payload.SignUpRequest;
import com.example.biblio.payload.TokenRefreshRequest;
import com.example.biblio.repository.UserRepository;
import com.example.biblio.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.net.URI;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider tokenProvider;
    RefreshTokenService refreshTokenService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional(readOnly = true)
    public Boolean existsByUsername(SignUpRequest user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        } else return true;
    }

    @Transactional(readOnly = true)
    public Boolean existsByEmail(SignUpRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false;
        } else return true;
    }

    @Transactional
    public URI registerUser(SignUpRequest signUpRequest) throws IOException {
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        JsonNode role = JacksonUtil.toJsonNode("{" +
                "\"name\": \"ROLE_USER\"" +
                "}");
        user.setRole(role);

        User newUser = userRepository.save(user);

        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(newUser.getUsername()).toUri();
    }

    public String createAccessToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication);
    }

    @Transactional
    public String createRefreshToken(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        RefreshToken refreshToken = refreshTokenService.create(user);

        user.setRefreshToken(refreshToken);

        refreshToken.setUser(user);
        refreshTokenService.save(refreshToken);

        return refreshToken.getToken();
    }

    @Transactional
    public JwtAuthenticationResponse refreshJwtToken(TokenRefreshRequest tokenRefreshRequest) {
        String token = tokenRefreshRequest.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.find(token);

        User user = refreshToken.getUser();
        refreshTokenService.verifyExpiration(refreshToken);

        String accessToken = tokenProvider.generateTokenFromId(user.getId());

        RefreshToken newRefreshToken = refreshTokenService.create(user);
        user.setRefreshToken(newRefreshToken);

        newRefreshToken.setUser(user);
        refreshTokenService.save(newRefreshToken);

        return new JwtAuthenticationResponse(accessToken, newRefreshToken.getToken());
    }

}
