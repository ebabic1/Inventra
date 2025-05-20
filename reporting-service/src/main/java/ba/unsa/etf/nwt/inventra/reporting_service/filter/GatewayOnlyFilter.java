package ba.unsa.etf.nwt.inventra.reporting_service.filter;

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

        boolean isSwaggerRequest = path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui.html");

        if ("true".equalsIgnoreCase(gatewayHeader) || isSwaggerRequest) {
            chain.doFilter(request, response);
            return;
        }

        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        res.setContentType("application/json");
        res.getWriter().write("{\"error\": \"Direct access denied. Use API Gateway.\"}");
        res.getWriter().flush();
    }
}
