package com.gabriel.todoapp.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class ResourceServerConfig {

    @Autowired
    private TodoAppSecurityProperties properties;

    private static final String[] AUTH_WHITELIST = {
            "/login", "/logout", "/oauth2/logout"
    };

    @Bean
    public SecurityFilterChain authSecurityFilter(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.POST, "/usuarios").anonymous()
                .and().authorizeRequests().anyRequest().authenticated()
                .and().logout()
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies()
                .and()
                    .csrf().disable()
                    .cors().and()
                .oauth2ResourceServer()
                    .jwt()
                    .jwtAuthenticationConverter(jwtAuthenticationConverter());

        http.logout(logoutConfig -> {
            logoutConfig.logoutSuccessHandler((request, response, authentication) -> {
                String returnTo = request.getParameter("returnTo");
                if (!StringUtils.hasText(returnTo)) {
                    returnTo = properties.getProviderUrl();
                }

                response.setStatus(302);
                response.sendRedirect(returnTo);
            });
        });

        http.formLogin(customizer -> customizer.loginPage("/login"));

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                jwt ->  {
                    List<String> userAuthorities = jwt.getClaimAsStringList("authorities");

                    if (userAuthorities.isEmpty()) {
                        userAuthorities = Collections.emptyList();
                    }

                    JwtGrantedAuthoritiesConverter scopesConverter = new JwtGrantedAuthoritiesConverter();
                    Collection<GrantedAuthority> scopesAuthorities = scopesConverter.convert(jwt);

                    scopesAuthorities
                            .addAll(userAuthorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));

                    return scopesAuthorities;
                });

        return jwtAuthenticationConverter;
    }
}
