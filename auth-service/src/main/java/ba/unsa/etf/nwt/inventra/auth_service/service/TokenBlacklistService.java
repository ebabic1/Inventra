package ba.unsa.etf.nwt.inventra.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final StringRedisTemplate redisTemplate;

    public void blacklistToken(String jti, long expirationMillis) {
        redisTemplate.opsForValue().set("blacklist:" + jti, "true", Duration.ofMillis(expirationMillis));
    }

    public boolean isTokenBlacklisted(String jti) {
        return Boolean.TRUE.toString().equals(redisTemplate.opsForValue().get("blacklist:" + jti));
    }
}
