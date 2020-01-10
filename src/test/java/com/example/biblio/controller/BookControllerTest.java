package com.example.biblio.controller;

import com.example.biblio.payload.LoginRequest;
import com.example.biblio.payload.modelRequest.BookRequest;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookControllerTest {

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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    public void getAllBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void getBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/80")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_ADMIN")
    public void deleteBookByIdIfAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/79")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_USER")
    public void deleteBookByIdIfNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/79")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_ADMIN")
    public void createBookIfAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setBookForCreateAndUpdate()))
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("message").value("Book created successfully"));
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_USER")
    public void createBookIfNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setBookForCreateAndUpdate()))
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isForbidden());
    }

    public BookRequest setBookForCreateAndUpdate(){
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("PHP");
        LocalDate date = LocalDate.parse("2018-11-13");
        bookRequest.setReleaseTime(date);

        return bookRequest;
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_ADMIN")
    public void updateBookIfAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/82")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setBookForCreateAndUpdate()))
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("message").value("Book updated successfully"));
    }

    @Test
    @WithMockUser(username = "Nooo", roles = "ROLE_USER")
    public void updateBookIfNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/82")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setBookForCreateAndUpdate()))
                .header("Authorization", accessToken)
                .header("RefreshToken", refreshToken))
                .andExpect(status().isForbidden());
    }

}