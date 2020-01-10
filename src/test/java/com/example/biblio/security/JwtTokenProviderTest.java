package com.example.biblio.security;

import com.example.biblio.payload.LoginRequest;
import com.example.biblio.util.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Clock;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenProviderTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    Clock clockMock;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    JwtProperties jwtProperties;

    String accessToken;

    String refreshToken;

    @Before
    public void setUp(){
       // accessToken = createAccessToken();

       // refreshToken = createRefreshToken()

    }

    public Authentication getAuthentication() {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("Nooo", "12345A!a"));

        return authentication;
    }

    public String createAccessToken() {
        return jwtTokenProvider.generateToken(
                getAuthentication(), jwtProperties.getExpirationAccessToken());
    }

    @Test
    public void testGenerateTokenGeneratesDifferentTokensForDifferentCreationDates() throws Exception {
        when(clockMock.now())
                .thenReturn(DateUtil.yesterday())
                .thenReturn(DateUtil.now());

        final String token = createAccessToken();
        final String laterToken = createAccessToken();

        assertThat(token).isNotEqualTo(laterToken);
    }

    @Test
    public void validateRefreshAndAccessTokens(){
        assertThat(jwtTokenProvider.validateToken(accessToken)).isTrue();
        assertThat(jwtTokenProvider.validateToken(refreshToken)).isTrue();
    }

    @Test
    public void checkExpirationAccessToken(){
        assertThat(jwtTokenProvider.checkExpirationAccessToken(accessToken)).isTrue();
    }

}
