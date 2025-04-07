package com.example.eLibrary.security;

import com.example.eLibrary.security.jwt.AuthTokenFilter;
import com.example.eLibrary.security.services.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final AuthenticationProvider authenticationProvider;


    @RequiredArgsConstructor
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        private final AuthenticationProvider authenticationProvider;
        private final CustomAuthenticationSuccessHandler successHandler; // 👈 добави това

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/register", "/auth/login", "/v1/books/**", "/v1/index", "/v1/catalog", "/css/**", "/images/**").permitAll()
                            .requestMatchers("/v1/basket/**").authenticated()
                            .requestMatchers("/v1/admin/**").hasRole("ADMIN")
                            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                            .requestMatchers("/api/v1/user/**").hasRole("USER")
                            .anyRequest().authenticated())
                    .formLogin(login -> login
                            .loginPage("/auth/login")
                            .successHandler(successHandler) // 👈 ето тук
                            .usernameParameter("email")
                            .permitAll())
                    .logout(logout -> logout
                            .logoutUrl("/auth/logout")
                            .logoutSuccessUrl("/auth/login?logout=true")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll())
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                            .maximumSessions(1).expiredUrl("/auth/login?expired=true"))
                    .authenticationProvider(authenticationProvider);

            return http.build();
        }
    }


    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjar/**"
    };

//  @Override
//  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//  }

    //@Bean
    //public DaoAuthenticationProvider authenticationProvider() {
    //    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //
    //    authProvider.setUserDetailsService(userDetailsService);
    //    authProvider.setPasswordEncoder(securityConfig.bCryptPasswordEncoder());
    //
    //    return authProvider;
    //}

//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }

    //@Bean
    //public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    //  return authConfig.getAuthenticationManager();
    //}

// @Bean
// public PasswordEncoder passwordEncoder() {
//   return new BCryptPasswordEncoder();
// }

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.cors().and().csrf().disable()
//      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//      .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//      .antMatchers("/api/test/**").permitAll()
//      .anyRequest().authenticated();
//
//    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//  }


}
