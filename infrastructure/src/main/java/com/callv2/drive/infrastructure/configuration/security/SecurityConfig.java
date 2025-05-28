package com.callv2.drive.infrastructure.configuration.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.callv2.drive.infrastructure.configuration.properties.cors.CorsConfigurationProperties;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ROLE_ADMIN = "ADMINISTRADOR";

    @Bean
    SecurityFilterChain securityFilterChain(
            final HttpSecurity http,
            final CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> {
                    authorize

                            .requestMatchers(HttpMethod.OPTIONS, "/**")
                            .permitAll()

                            .requestMatchers("admin/**")
                            .hasAnyRole(ROLE_ADMIN)

                            .requestMatchers(HttpMethod.GET,
                                    "swagger-ui.html",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html")
                            .permitAll()

                            .anyRequest()
                            .authenticated();
                })
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakJwtConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(final CorsConfigurationProperties corsProperties) {

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                corsProperties.getPattern(),
                corsConfiguration(
                        corsProperties.getAllowedOrigins(),
                        corsProperties.getAllowedMethods(),
                        corsProperties.getAllowedHeaders(),
                        corsProperties.isAllowCredentials()));

        return source;
    }

    static CorsConfiguration corsConfiguration(
            final List<String> allowedOriginsPatterns,
            final List<String> allowedMethods,
            final List<String> allowedHeaders,
            final boolean allowCredentials) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(allowedOriginsPatterns);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowCredentials(allowCredentials);
        return configuration;
    }

    @Bean
    @ConfigurationProperties("security.cors")
    CorsConfigurationProperties corsConfigurationProperties() {
        return new CorsConfigurationProperties();
    }

}
