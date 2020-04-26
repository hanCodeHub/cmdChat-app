package com.handev.cmdChat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security config class with boilerplate code for customizing authentication
 *
 * @see <a href="https://spring.io/guides/tutorials/spring-boot-oauth2/">Spring Boot OAuth2</a>
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Defines which endpoints are allowed and which ones are secured.
   *
   * @param http HttpSecurity object with authentication config methods
   * @throws Exception HTTP Exception caught when client communiates with server
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.headers().frameOptions().sameOrigin(); // allow same origin requests

    http.csrf(
            c ->
                c // creates csrf cookie for frontend client
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    // ignores csrf for these endpoints
                    .ignoringAntMatchers("/h2-console/**", "/logout"))
        .authorizeRequests(
            a ->
                a // customize secured endpoints
                    .antMatchers(
                        "/",
                        "/error",
                        "/webjars/**",
                        "/user/ping",
                        "/user/register",
                        "/js/**",
                        "/img/**",
                        "/css/**",
                        "/h2-console/**")
                    .permitAll() // allow these endpoints and static assets
                    .anyRequest()
                    .authenticated() // any other endpoints secured
            )
        // return 401 if unauthorized
        .exceptionHandling(
            e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .oauth2Login()
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/")
        .permitAll();
  }
}
