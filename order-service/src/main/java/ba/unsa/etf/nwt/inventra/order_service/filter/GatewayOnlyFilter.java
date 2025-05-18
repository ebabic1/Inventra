package ba.unsa.etf.nwt.inventra.order_service.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
public class GatewayOnlyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String gatewayHeader = req.getHeader("X-Gateway-Auth");

        if ("true".equals(gatewayHeader)) {
            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui.html")) {
            chain.doFilter(request, response);
            return;
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Direct access denied. Use API Gateway.");
    }
}
