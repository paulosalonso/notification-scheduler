package com.github.paulosalonso.notification.application.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Profile({ "secure-api", "secure-api-jwk" })
@Configuration
public class SecureResourceServerConfiguration extends WebSecurityConfigurerAdapter {

    public SecureResourceServerConfiguration() {
        log.info("Starting application with security");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    @Override
    public void configure(WebSecurity web) {
        permitSwagger(web);
        permitActuator(web);
    }

    private void permitSwagger(WebSecurity web) {
        web.ignoring().antMatchers("/v3/api-docs", "/swagger-ui/**", "/swagger-resources/**");
    }

    private void permitActuator(WebSecurity web) {
        web.ignoring().antMatchers("/actuator/info", "/actuator/health", "/actuator/prometheus");
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

}
