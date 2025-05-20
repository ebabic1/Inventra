package ba.unsa.etf.nwt.inventra.api_gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final StringRedisTemplate redisTemplate;

    public boolean isTokenBlacklisted(String jti) {
        return Boolean.TRUE.toString().equals(redisTemplate.opsForValue().get("blacklist:" + jti));
    }
}
