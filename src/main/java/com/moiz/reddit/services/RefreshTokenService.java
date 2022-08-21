package com.moiz.reddit.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moiz.reddit.Exceptions.SpringRedditEception;
import com.moiz.reddit.Model.RefreshToken;
import com.moiz.reddit.repository.RefreshTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	
	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());
		
		return refreshTokenRepository.save(refreshToken);
		
	}
	
	void validateRefreshToken(String token) {
		refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new SpringRedditEception("Invalid Refresh Token"));
	}
	
	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}
}
