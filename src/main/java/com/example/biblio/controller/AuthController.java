package com.example.biblio.controller;

import com.example.biblio.payload.ApiResponse;
import com.example.biblio.payload.JwtAuthenticationResponse;
import com.example.biblio.payload.LoginRequest;
import com.example.biblio.payload.SignUpRequest;
import com.example.biblio.security.JwtTokenProvider;
import com.example.biblio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
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
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        // I made single return, but how then new user be able to know what is exactly already taken ???
        if ((userService.existsByUsername(signUpRequest.getUsername())) || (userService.existsByEmail(signUpRequest.getEmail()))) {
            return new ResponseEntity(new ApiResponse(false, "Username or Email is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        URI location = userService.registerUser(signUpRequest);

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
