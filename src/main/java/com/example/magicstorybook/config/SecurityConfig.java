package com.example.magicstorybook.config;

import com.example.magicstorybook.handler.CustomOAuth2LoginSuccessHandler;
import com.example.magicstorybook.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = Logger.getLogger(SecurityConfig.class.getName());

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2LoginSuccessHandler successHandler) throws Exception {
        logger.info("Configuring SecurityFilterChain");
        http
                .authorizeHttpRequests(authorizeRequests -> {
                    logger.info("Configuring authorization requests");
                    authorizeRequests
                            .requestMatchers("/", "/error", "/webjars/**", "/signup", "/api/user").permitAll()
                            .requestMatchers("/api/user/**", "/api/stories/**").authenticated()  // Secure API endpoints
                            .anyRequest().authenticated();
                })
                .oauth2Login(oauth2Login -> {
                    logger.info("Configuring OAuth2 login");
                    oauth2Login
                            .defaultSuccessUrl("/", true)
                            .successHandler(successHandler)  // Use injected success handler
                            .userInfoEndpoint(userInfoEndpoint -> {
                                logger.info("Configuring user info endpoint");
                                userInfoEndpoint.userService(oauth2UserService());
                            });
                })
                .logout(logout -> {
                    logger.info("Configuring logout");
                    logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/signup")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll();
                })
                .csrf(csrf -> {
                    logger.info("Disabling CSRF protection for API endpoints");
                    csrf.ignoringRequestMatchers("/api/**");
                });

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        logger.info("Creating CustomOAuth2UserService bean");
        return new CustomOAuth2UserService();
    }

    @Bean
    public CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler(OAuth2AuthorizedClientService authorizedClientService) {
        return new CustomOAuth2LoginSuccessHandler(authorizedClientService);
    }
}
