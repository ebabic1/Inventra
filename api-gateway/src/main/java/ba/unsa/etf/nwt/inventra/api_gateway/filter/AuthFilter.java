package ba.unsa.etf.nwt.inventra.api_gateway.filter;

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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/auth/api")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.validateToken(token);
            String role = claims.get("role", String.class);
            String userId = claims.getSubject();

            if (path.startsWith("/order/api") && !"WAREHOUSE_MANAGER".equals(role)) {
                return onError(exchange, "Access denied for non WAREHOUSE_MANAGER users", HttpStatus.FORBIDDEN);
            }

            if (path.startsWith("/inventory/api") && !"WAREHOUSE_OPERATOR".equals(role)) {
                return onError(exchange, "Access denied for non WAREHOUSE_OPERATOR users", HttpStatus.FORBIDDEN);
            }

            if (path.startsWith("/reporting/api") && !"ADMIN".equals(role)) {
                return onError(exchange, "Access denied for non ADMIN users", HttpStatus.FORBIDDEN);
            }

            exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role)
                    .build();

            return chain.filter(exchange);

        } catch (Exception e) {
            return onError(exchange, "Invalid JWT Token", HttpStatus.UNAUTHORIZED);
        }
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
