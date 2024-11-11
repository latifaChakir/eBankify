package com.example.ebankify.security;

import com.example.ebankify.domain.enums.Role;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class SessionAuthenticationFilter extends OncePerRequestFilter {
    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/register"
    );

    private boolean isPublicEndpoint(String servletPath) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(endpoint ->
                servletPath.startsWith(endpoint) ||
                        servletPath.matches(endpoint.replace("*", ".*"))
        );
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if (isPublicEndpoint(servletPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("name") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized: No active session\"}");
            response.getWriter().flush();
            return;
        }

        Role role = (Role) session.getAttribute("role");
        if (role == null || !RolePermissions.hasPermission(role.name(), servletPath)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Forbidden: Insufficient permissions\"}");
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
