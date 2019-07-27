package com.example.biblio.service;

import com.example.biblio.exception.TokenRefreshException;
import com.example.biblio.model.User;
import com.example.biblio.model.token.RefreshToken;
import com.example.biblio.repository.RefreshTokenRepository;
import com.example.biblio.security.JwtTokenProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;
    JwtTokenProvider tokenProvider;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtTokenProvider tokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshToken find(String token) {
        return refreshTokenRepository.findRefreshTokenByToken(token);
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpirationDate() - new Date().getTime() < 0) {
            throw new TokenRefreshException(token.getToken(), "Expired token. Please issue a new request");
        }
    }

    public RefreshToken create(User user) {
        RefreshToken refreshToken;

        if (user.getRefreshToken() != null) {
            refreshToken = user.getRefreshToken();
        } else {
            refreshToken = new RefreshToken();
        }

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpirationDate(tokenProvider.getExpirationTime());

        return refreshToken;
    }

}
