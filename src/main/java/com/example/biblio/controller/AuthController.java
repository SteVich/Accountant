package com.example.biblio.controller;

import com.example.biblio.payload.ApiResponse;
import com.example.biblio.payload.JwtAuthenticationResponse;
import com.example.biblio.payload.LoginRequest;
import com.example.biblio.payload.SignUpRequest;
import com.example.biblio.payload.TokenRequest;
import com.example.biblio.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    AuthenticationManager authenticationManager;
    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        ApiResponse apiResponse;

        URI location = null;

        if (!authService.existsByUsername(signUpRequest)) {
            apiResponse = new ApiResponse(false, "Username  is already taken!");
        } else if (!authService.existsByEmail(signUpRequest)) {
            apiResponse = new ApiResponse(false, "Email  is already taken!");
        } else {
            apiResponse = new ApiResponse(true, "User registered successfully");
            location = authService.registerUser(signUpRequest);
        }

        return ResponseEntity.created(location).body(apiResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = authService.createAccessToken(authentication);
        String refreshToken = authService.createRefreshToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(accessToken, refreshToken));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity refreshJwtToken(@Valid @RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.generateNewTokens(tokenRequest));
    }

}

