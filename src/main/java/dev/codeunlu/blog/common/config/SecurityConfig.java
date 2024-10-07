package dev.codeunlu.blog.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITE_LIST_URL)
            .permitAll()
            .anyRequest()
            .authenticated())
        .formLogin(login -> {
          login.loginPage("/auth/sign-in");
          login.successForwardUrl("/");
          login.permitAll();
        })
        .logout(logout -> {
          logout.addLogoutHandler(logoutHandler);
          logout.logoutUrl("/api/v1/auth/logout");
          logout.logoutSuccessUrl("/");
          logout.logoutSuccessHandler(
              (_, _, _) -> SecurityContextHolder.clearContext());
        })
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  private final String[] WHITE_LIST_URL = {
      "swagger-ui/**",
      "swagger-ui.html",
      "webjars/**",
      "/v2/api-docs",
      "/v3/api-docs",
      "/v3/api-docs/**",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/configuration/security",
      "/css/**",
      "/images/**",
      "/api/v1/auth/**",
      "/auth/**",
  };

}
