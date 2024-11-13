package com.example.demo.authentication;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFiler extends OncePerRequestFilter {
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    TokenHelper tokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOGGER.debug("Inside AuthenticationFiler: " + request.getRequestURI());
        String token = extractTokenFromHeader(request, response);
        if(ignoreApi(request)){
            filterChain.doFilter(request, response);
        }
        if (token != null) {
            LOGGER.debug("doFilterInternal()-Token found in header");
            boolean isValid=tokenHelper.validateTokenSignature(token);

            if(isValid) {
                LOGGER.debug("doFilterInternal()-Token is valid");
                filterChain.doFilter(request, response);
            }
            else {
                LOGGER.error("doFilterInternal()-Token is not valid");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new SignatureException("JWT access token is invalid");
            }
        }
        else {
            LOGGER.error("doFilterInternal()-Token not found in header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new SignatureException("JWT access token is null");
        }
    }

    private boolean ignoreApi(HttpServletRequest request){
        String url=request.getRequestURI();
        if(url.contains("/login")){
            return true;
        }
        return false;
    }

    private String extractTokenFromHeader(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        return token;
    }
}
