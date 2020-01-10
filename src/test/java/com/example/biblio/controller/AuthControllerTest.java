package com.example.biblio.controller;

import com.example.biblio.payload.LoginRequest;
import com.example.biblio.payload.SignUpRequest;
import com.example.biblio.payload.TokenRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String refreshToken;

    @InjectMocks
    TokenRequest tokenRequest;

    @Before
    public void setUp() throws Exception {
        final MvcResult result = mockMvc.perform(post("/api/auth/signin")
                .content(objectMapper.writeValueAsString(setLoginCredentials()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("refreshToken").isNotEmpty())
                .andReturn();

        tokenRequest.setRefreshToken(result.getResponse().getContentAsString().substring(202, 370));
    }

    public SignUpRequest setUserDataForLogin() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("Oleg");
        signUpRequest.setUsername("Nooo");
        signUpRequest.setEmail("nooo@gmail.com");
        signUpRequest.setPassword("12345A!a");

        return signUpRequest;
    }

    @Test
    public void signUpIfAllDataIsCorrect() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setUserDataForLogin())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("message").value("User registered successfully"));
    }

    @Test
    public void signUpIfUsernameIsExist() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(setUserDataForLogin()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("message").value("Username  is already taken!"));
    }

    @Test
    public void signUpIfEmailIsExist() throws Exception {

        mockMvc.perform(post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(setUserDataForLogin()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("message").value("Email  is already taken!"));
    }

    @Test
    public void signUpIfPasswordIncorrect() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(setUserDataForLogin()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    public LoginRequest setLoginCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("Nooo");
        loginRequest.setPassword("12345A!a");

        return loginRequest;
    }

    @Test
    public void signInIfAllCredentialsIsCorrect() throws Exception {
        mockMvc.perform(post("/api/auth/signin")
                .content(objectMapper.writeValueAsString(setLoginCredentials()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void signInIfUsernameOrEmailNotFound() throws Exception {
        mockMvc.perform(post("/api/auth/signin")
                .content(objectMapper.writeValueAsString(setLoginCredentials()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void signInIfBadPassword() throws Exception {
        mockMvc.perform(post("/api/auth/signin")
                .content(objectMapper.writeValueAsString(setLoginCredentials()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void getRefreshToken() throws Exception {
        mockMvc.perform(post("/api/auth/refreshToken")
                .content(objectMapper.writeValueAsString(tokenRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}

