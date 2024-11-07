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

        // Vérifiez la session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("name") == null) {
            System.out.println("No active session found");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: No active session");
            return;
        }

        Role role = (Role) session.getAttribute("role");
        System.out.println("Role from session: " + role);
        if (role == null || !RolePermissions.hasPermission(role.name(), servletPath)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: Insufficient permissions");
            return;
        }

        filterChain.doFilter(request, response);
    }


}
