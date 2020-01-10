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
import org.springframework.security.test.context.support.WithUserDetails;
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
public class BorrowedControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String accessToken;

    String refreshToken;

    @Before
    public void setUp() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/signin")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/borroweds"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser("ROLE_ADMIN")
    public void getAllBorrowedsIfAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/borroweds")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser("ROLE_USER")
    public void getAllBBorrowedsIfNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/borroweds")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails
    public void getBorrowedByIdIfUserBorrowsThisBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/borroweds/85")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails
    public void getBorrowedByIdIfUserDoesNotBorrowThisBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/borroweds/85")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void deleteBorrowed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/borroweds/84")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    public void createBorrowed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books/borroweds/83")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("message").value("You successfully borrowed a book"));
    }

}