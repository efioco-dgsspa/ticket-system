package com.efioco.ticketsystem.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.efioco.ticketsystem.enums.Role;
import com.efioco.ticketsystem.security.JwtAuthenticationEntryPoint;
import com.efioco.ticketsystem.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // ======================
    // 🔓 Endpoint pubblici
    // ======================
    private static final String[] PUBLIC_ENDPOINTS = {
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/v3/api-docs",
        "/api/auth/login",
        "/api/auth/refresh"
    };

    // ======================
    // 🔒 Endpoint protetti
    // ======================
    private static final String[] USER_GET_ENDPOINTS = {
        "/api/users",
        "/api/users/{id}",
        "/api/users/by-username/**",
        "/api/users/by-email/**"
    };

    private static final String[] USER_POST_ENDPOINTS = {
        "/api/users"
    };

    private static final String[] USER_PUT_ENDPOINTS = {
        "/api/users/update-user"
    };

    private static final String[] USER_DELETE_ENDPOINTS = {
        "/api/users/{id}"
    };

    private static final String[] USER_PATCH_ACTIVATION_ENDPOINTS = {
        "/api/users/{id}/activate",
        "/api/users/{id}/deactivate"
    };
    
    // ======================
    // 🎫 TICKETS
    // ======================
    private static final String[] TICKET_GET_ENDPOINTS = { 
    	"/api/tickets", "/api/tickets/{id}" 
    };
    
    private static final String[] TICKET_POST_ENDPOINTS = { 
    	"/api/tickets" 
    };
    
    private static final String[] TICKET_PUT_ENDPOINTS = { 
    	"/api/tickets/{id}" 
    };
    
    private static final String[] TICKET_DELETE_ENDPOINTS = { 
    	"/api/tickets/{id}" 
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults()) // ✅ abilita CORS
            .authorizeHttpRequests(auth -> {
                configurePublicEndpoints(auth);
                configureUserEndpoints(auth);
                configureTicketEndpoints(auth);
                auth.anyRequest().authenticated();
            })
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
	// ======================
    // 🔓 PUBLIC ENDPOINTS
    // ======================
    private void configurePublicEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll();
    }
    
    // ======================
    // 👤 USERS ENDPOINTS RULES
    // ======================
    private void configureUserEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(HttpMethod.GET, USER_GET_ENDPOINTS)
                .hasAnyRole(Role.ADMIN.name(), Role.USER.name(), Role.SUPPORT_MANAGER.name());
        auth.requestMatchers(HttpMethod.POST, USER_POST_ENDPOINTS)
                .hasAnyRole(Role.ADMIN.name(), Role.SUPPORT_MANAGER.name());
        auth.requestMatchers(HttpMethod.PUT, USER_PUT_ENDPOINTS)
                .hasAnyRole(Role.ADMIN.name(), Role.USER.name(), Role.SUPPORT_MANAGER.name());
        auth.requestMatchers(HttpMethod.DELETE, USER_DELETE_ENDPOINTS)
                .hasAnyRole(Role.ADMIN.name(), Role.SUPPORT_MANAGER.name());
        auth.requestMatchers(HttpMethod.PATCH, USER_PATCH_ACTIVATION_ENDPOINTS)
                .hasAnyRole(Role.ADMIN.name(), Role.SUPPORT_MANAGER.name());
    }

    // ======================
    // 🎫 TICKETS ENDPOINTS RULES
    // ======================
    private void configureTicketEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(HttpMethod.GET, TICKET_GET_ENDPOINTS)
                .hasAnyRole(Role.ADMIN.name(), Role.SUPPORT_MANAGER.name(), Role.USER.name());
        auth.requestMatchers(HttpMethod.POST, TICKET_POST_ENDPOINTS)
                .hasAnyRole(Role.ADMIN.name(), Role.USER.name());
        auth.requestMatchers(HttpMethod.PUT, TICKET_PUT_ENDPOINTS)
                .hasAnyRole(Role.ADMIN.name(), Role.SUPPORT_MANAGER.name());
        auth.requestMatchers(HttpMethod.DELETE, TICKET_DELETE_ENDPOINTS)
                .hasAnyRole(Role.ADMIN.name(), Role.SUPPORT_MANAGER.name());
    }

    // ======================
    // 🧩 Beans utili
    // ======================

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ======================
    // 🌍 CORS Configuration
    // ======================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
