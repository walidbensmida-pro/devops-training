package com.harington.devops_training.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration de la sécurité Spring Boot pour l'intégration Keycloak (OIDC).
 *
 * - Seuls les endpoints /wiki-ai et /api/ask-wiki nécessitent une authentification Keycloak (OIDC).
 * - Toutes les autres routes (accueil, contact, etc.) restent publiques.
 * - Utilise le mécanisme oauth2Login de Spring Security (redirection automatique vers Keycloak si besoin).
 * - Le logout redirige vers la page d'accueil après déconnexion.
 * - CSRF est désactivé (à réactiver si tu fais des POST côté navigateur).
 *
 * Pour ajouter d'autres routes protégées, ajoute-les dans requestMatchers().
 * Pour rendre tout le site privé, remplace .anyRequest().permitAll() par .anyRequest().authenticated().
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Déclare la chaîne de filtres de sécurité.
     *
     * @param http la configuration HttpSecurity
     * @return la SecurityFilterChain configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http
            // Protège uniquement /wiki-ai et /api/ask-wiki (authentification requise)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/wiki-ai", "/api/ask-wiki").authenticated()
                .anyRequest().permitAll() // le reste est public
            )
            // Active l'authentification OAuth2 Login (Keycloak)
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization -> authorization
                    .authorizationRequestResolver(
                        customAuthorizationRequestResolver(clientRegistrationRepository)
                    )
                )
            )
            // Configure le logout (redirection vers / après déconnexion)
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            )
            // Désactive CSRF (à activer si besoin)
            .csrf(csrf -> csrf.disable());
        return http.build();
    }

    private OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(ClientRegistrationRepository repo) {
        DefaultOAuth2AuthorizationRequestResolver defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");
        return new OAuth2AuthorizationRequestResolver() {
            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
                OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request);
                if (authRequest == null) return null;
                Map<String, Object> extraParams = new HashMap<>(authRequest.getAdditionalParameters());
                extraParams.put("prompt", "login");
                return OAuth2AuthorizationRequest.from(authRequest)
                        .additionalParameters(extraParams)
                        .build();
            }
            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
                OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request, clientRegistrationId);
                if (authRequest == null) return null;
                Map<String, Object> extraParams = new HashMap<>(authRequest.getAdditionalParameters());
                extraParams.put("prompt", "login");
                return OAuth2AuthorizationRequest.from(authRequest)
                        .additionalParameters(extraParams)
                        .build();
            }
        };
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
