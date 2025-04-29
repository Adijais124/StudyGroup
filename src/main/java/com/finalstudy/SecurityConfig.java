    package com.finalstudy;

    import org.springframework.context.annotation.Bean;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.web.SecurityFilterChain;

    @org.springframework.context.annotation.Configuration
    public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable()) // Updated syntax for disabling CSRF
                .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().permitAll() // Allows all requests
                );
            return http.build();
        }
    }