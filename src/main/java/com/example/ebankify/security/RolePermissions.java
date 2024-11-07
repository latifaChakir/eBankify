package com.example.ebankify.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RolePermissions {
    private static final Map<String, Set<String>> permissionsMap = new HashMap<>();

    static {
        // Permissions pour le rôle ADMIN
        Set<String> adminPermissions = new HashSet<>();
        adminPermissions.add("/api/users");
        adminPermissions.add("/api/users/save");
        adminPermissions.add("/api/users/delete/{id}");
        adminPermissions.add("/api/accounts/save");
        adminPermissions.add("/api/transactions");
        adminPermissions.add("/api/transactions/save");
        permissionsMap.put("ADMIN", adminPermissions);

        // Permissions pour le rôle EMPLOYEE
        Set<String> employeePermissions = new HashSet<>();
        employeePermissions.add("/api/accounts/all");
        permissionsMap.put("EMPLOYEE", employeePermissions);

        // Permissions pour le rôle USER
        Set<String> userPermissions = new HashSet<>();
        userPermissions.add("/api/accounts/save");
        userPermissions.add("/api/transactions/save");
        permissionsMap.put("USER", userPermissions);
    }

    public static boolean hasPermission(String role, String servletPath) {
        System.out.println("Role: " + role);
        System.out.println("Requested Path: " + servletPath);
        return permissionsMap.containsKey(role) && permissionsMap.get(role).stream().anyMatch(pattern ->
                servletPath.matches(pattern.replace("{id}", "\\d+").replace("*", ".*"))
        );
    }

}
