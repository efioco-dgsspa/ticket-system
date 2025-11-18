package com.efioco.ticketsystem.security;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.efioco.ticketsystem.exceptions.UserServiceException;
import com.efioco.ticketsystem.response.UserResponse;
import com.efioco.ticketsystem.service.AuthService;
import com.efioco.ticketsystem.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UserService userService;
    
    @Autowired
    @Lazy
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
    	
        String path = request.getServletPath();
//        System.out.println("🔵 JwtAuthenticationFilter - Path: " + path);
        
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/refresh")) {
//        	System.out.println("⏭️ Skippando filtro per: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        UserResponse userResponse = new UserResponse();
        UserDetails userDetails = null;
        String email = null;
        String jwt = null;

        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            email = jwtUtil.getEmailFromToken(jwt);
        }

        if (StringUtils.isNotBlank(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                userResponse = userService.getUserByEmail(email);
            } catch (UserServiceException e) {
                e.printStackTrace();
            }

			try {
				userDetails = authService.getUserDetails(userResponse.getUser());
			} catch (UserServiceException e) {
				 e.printStackTrace();
			}

            if (jwtUtil.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
    
}
