package net.davidvoid.thor.lightning.service.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.davidvoid.thor.lightning.exception.UnauthorizedAccessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {
    
    @Autowired
    JwtAuthenticationService jwtService = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            Authentication authentication = attemptAuthentication(req, res);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (UnauthorizedAccessException e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }

    private Authentication attemptAuthentication(HttpServletRequest request,
                                                 HttpServletResponse response) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        String jwt_token = header.substring(7);
        return jwtService.authenticate(jwt_token);
    }
}
