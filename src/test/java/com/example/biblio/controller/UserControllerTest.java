package com.example.biblio.controller;

import com.example.biblio.payload.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String accessToken;

    String refreshToken;

    @Before
    public void setUp() throws Exception {

        final MvcResult result = mockMvc.perform(post("/api/auth/signin")
                .content(objectMapper.writeValueAsString(login()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").isNotEmpty())
                .andExpect(jsonPath("refreshToken").isNotEmpty())
                .andReturn();

        accessToken = result.getResponse().getContentAsString().substring(16, 184);
        refreshToken = result.getResponse().getContentAsString().substring(202, 370);
    }

    public LoginRequest login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("Nooo");
        loginRequest.setPassword("12345A!a");

        return loginRequest;
    }

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_ADMIN")
    public void getAllUsersIfAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().is2xxSuccessful());

        System.out.println(accessToken);
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_USER")
    public void getAllUsersIfNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_ADMIN")
    public void getUserByIdIfAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/9")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_USER")
    public void getUserByIdIfNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/9")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_ADMIN")
    public void deleteUserByIdIfAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/6")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_USER")
    public void deleteUserByIdIfNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/6")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isNoContent());
    }

}
