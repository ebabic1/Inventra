package ba.unsa.etf.nwt.inventra.api_gateway.filter;

import ba.unsa.etf.nwt.inventra.api_gateway.service.TokenBlacklistService;
import ba.unsa.etf.nwt.inventra.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklistService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);
        if (token == null) {
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        try {
            Claims claims = jwtUtil.validateToken(token);
            if (isTokenRevoked(claims)) {
                return onError(exchange, "Token has been revoked", HttpStatus.UNAUTHORIZED);
            }

            if (!hasAccess(path, claims)) {
                return onError(exchange, "Access denied", HttpStatus.FORBIDDEN);
            }

            enrichRequestWithClaims(exchange, claims);

            return chain.filter(exchange);

        } catch (Exception e) {
            return onError(exchange, "Invalid JWT Token", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isPublicPath(String path) {
        return path.equals("/auth/api/users/login") || path.equals("/auth/api/users/register");
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    private boolean isTokenRevoked(Claims claims) {
        String jti = claims.getId();
        return blacklistService.isTokenBlacklisted(jti);
    }

    private boolean hasAccess(String path, Claims claims) {
        String role = claims.get("role", String.class);

        // Always allow logout
        if (path.equals("/auth/api/users/logout")) {
            return true;
        }

        // Always allow user info route
        if (path.equals("/auth/api/users/me")) {
            return true;
        }

        // Admin has full access
        if ("ADMIN".equals(role)) {
            return true;
        }

        // Prevent non-admins from changing roles
        if (path.matches("^/auth/api/users/.+/role$")) {
            return false;
        }

        return switch (role) {
            case "WAREHOUSE_MANAGER" -> path.startsWith("/order/api") || path.startsWith("/reporting/api");
            case "WAREHOUSE_OPERATOR" -> path.startsWith("/inventory/api");
            default -> false;
        };
    }

    private void enrichRequestWithClaims(ServerWebExchange exchange, Claims claims) {
        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-User-Role", role)
                .build();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String msg, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
