package com.highload.authservice.security;

import com.highload.feign.reactive_client.UserClient;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private final JwtProvider jwtProvider;
    private final UserClient userClient;

    private String parseJwt(jakarta.servlet.http.HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
            return headerAuth.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    @Override
    @SneakyThrows
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
        String jwt = parseJwt(request);
        if (jwt != null && jwtProvider.validateJwtToken(jwt)) {
            UUID userId = jwtProvider.getUserIdFromJwtToken(jwt);

            UserDetails userDetails = (UserDetails) userClient.getUserById(userId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
