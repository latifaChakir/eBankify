package com.example.ebankify.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Configuration
@Slf4j
public class KeycloakConfig {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret; // Utilisé si nécessaire pour les clients privés

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    // Initialise le client Keycloak pour obtenir un token
    @Bean
    public Keycloak keycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    // Récupérer un access token
    public String getAccessToken(String username, String password) {
        // Construire l'URL pour obtenir le jeton
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", authServerUrl, realm);

        // Créer le WebClient pour faire la requête
        WebClient webClient = WebClient.builder()
                .baseUrl(tokenUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        // Faire la requête pour obtenir le jeton
        String response = webClient.post()
                .bodyValue("grant_type=password&username=" + username + "&password=" + password + "&client_id=" + clientId + "&client_secret=" + clientSecret)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Bloque jusqu'à obtenir la réponse

        // Extraire le token d'accès de la réponse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            return (String) responseMap.get("access_token");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la récupération du jeton d'accès", e);
        }
    }

}