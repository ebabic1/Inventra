package ba.unsa.etf.nwt.inventra.auth_service.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
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

        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        res.getWriter().write("Direct access denied. Use API Gateway.");
    }
}

