package com.diploma.git.backend;

import jakarta.annotation.security.DeclareRoles;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.jcajce.provider.symmetric.TEA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String jwkSetUri;
    public static final String TEACHER = "teacher";
    public static final String STUDENT = "student";
    private final JwtConverter jwtConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) ->
                authz.requestMatchers(HttpMethod.GET, "/api/greetings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tutor/**").hasRole(TEACHER)
                        .requestMatchers(HttpMethod.GET, "/api/student/**").hasRole(STUDENT)
                        .requestMatchers(HttpMethod.GET, "/api/team/**").hasAnyRole(TEACHER,STUDENT)
                        .requestMatchers(HttpMethod.GET, "/api/project").hasAnyRole(TEACHER,STUDENT)
                        .requestMatchers(HttpMethod.GET,"/oauth2/**").permitAll()
                        .anyRequest().authenticated());

        http.sessionManagement(sess -> sess.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

        return http.build();
    }
}
