package com.example.eLibrary.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Getter
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    // private final UserDetailsServiceImpl userDetailsService;

    //private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("User authenticated: " + userDetails.getUsername() + " with roles: " + userDetails.getAuthorities());
            } else {
                System.out.println("Invalid JWT token for user: " + userDetails.getUsername() + " with roles: " + userDetails.getAuthorities());
            }
        }

        filterChain.doFilter(request, response);

    }
}
//try {
//  String jwt = parseJwt(request);
//  if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//    String email = jwtUtils.getEmailFromJwtToken(jwt);
//
//    UserDetails userDetails = userDetailsService.loadUserByEmail(email);
//
//    UsernamePasswordAuthenticationToken authentication =
//        new UsernamePasswordAuthenticationToken(userDetails,
//                                                null,
//                                                userDetails.getAuthorities());
//
//    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//  }
//} catch (Exception e) {
//  logger.error("Cannot set user authentication: {}", e);
//}
//
//filterChain.doFilter(request, response);
//}

//private String parseJwt(HttpServletRequest request) {
//  String jwt = jwtUtils.getJwtFromCookies(request);
//  return jwt;
//}
//}
