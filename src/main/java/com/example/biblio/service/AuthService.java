package com.example.biblio.service;

import com.example.biblio.model.Role;
import com.example.biblio.model.User;
import com.example.biblio.payload.JwtAuthenticationResponse;
import com.example.biblio.payload.SignUpRequest;
import com.example.biblio.payload.TokenRequest;
import com.example.biblio.repository.UserRepository;
import com.example.biblio.security.JwtTokenProvider;
import com.example.biblio.util.JwtProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider tokenProvider;
    JwtProperties properties;

    @Transactional(readOnly = true)
    public Boolean existsByUsername(SignUpRequest user) {
        return  userRepository.existsByUsername(user.getUsername()) ? false : true;
    }

    @Transactional(readOnly = true)
    public Boolean existsByEmail(SignUpRequest user) {
        return  userRepository.existsByEmail(user.getEmail()) ? false : true;
    }

    @Transactional
    public URI registerUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));

        User newUser = userRepository.save(user);

        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(newUser.getUsername()).toUri();
    }

    public String createAccessToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication, properties.getExpirationAccessToken());
    }

    public String createRefreshToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication, properties.getExpirationRefreshToken());
    }

    public JwtAuthenticationResponse generateNewTokens(TokenRequest tokenRequest) {
        Long userId = tokenProvider.getUserIdFromJWT(tokenRequest.getRefreshToken());

        String newAccessToken = tokenProvider.generateTokenFromId(userId, properties.getExpirationAccessToken());
        String newRefreshToken = tokenProvider.generateTokenFromId(userId, properties.getExpirationRefreshToken());

        return new JwtAuthenticationResponse(newAccessToken, newRefreshToken);
    }

}
