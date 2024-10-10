package com.example.eLibrary.security;

import com.example.eLibrary.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final AuthTokenFilter authTokenFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/api/v1/auth/**"))  // Изключва CSRF за API-тата, които използват JWT токени
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll()  // Оставя регистрационната форма достъпна за всички
                    .requestMatchers("/api/v1/auth/**").permitAll()  // Оставя API-тата за регистрация/логин отворени
                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")  // Администраторски API-та
                    .requestMatchers("/api/v1/user/**").hasRole("USER")  // Потребителски API-та
                    .anyRequest().authenticated())  // Всички останали пътища изискват автентикация
            .formLogin(form -> form
                    .loginPage("/auth/login")  // Можеш да добавиш логин страница по-късно
                    .permitAll())
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // JWT не изисква сесии
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
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
