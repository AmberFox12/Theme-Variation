package com.themevariation.backend.config;

import com.themevariation.backend.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/inscriptions/publique").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cours").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/historique", "/api/historique/page").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/historique").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/historique/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/historique/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/spectacles").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/spectacles").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/spectacles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/spectacles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/spectacles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/agenda/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/agenda/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/agenda/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/agenda/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/cours/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/types-danse/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/cours/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/cours/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/cours/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/types-danse/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/inscriptions").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/inscriptions").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/inscriptions/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/inscriptions/**").hasRole("ADMIN")
                .requestMatchers("/api/comptes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/eleves").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/eleves").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/eleves/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/parametres").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/parametres").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/contact").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/contact/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
