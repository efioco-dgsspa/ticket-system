package com.efioco.ticketsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.efioco.ticketsystem.dto.PermissionDTO;
import com.efioco.ticketsystem.dto.UserDTO;
import com.efioco.ticketsystem.exceptions.UserServiceException;
import com.efioco.ticketsystem.response.UserResponse;

@Service
public class AuthService implements AuthServiceInterface, UserDetailsService {

	@Autowired
	@Lazy
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void authenticate(String email, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
	}
	
	@Override
	public UserDetails getUserDetails(UserDTO user) throws UserServiceException {
		return userDetailsBuilder(userService.getUserByEmail(user.getEmail()));
	}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserResponse userResponse = userService.getUserByEmail(email);
            return userDetailsBuilder(userResponse);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Utente con email: " + email + " non trovato.");
        }
    }

    private UserDetails userDetailsBuilder(UserResponse userResponse) {
    	// ✅ 1️⃣ Mappa i permessi del ruolo
        List<SimpleGrantedAuthority> authorities = userResponse.getUser().getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(PermissionDTO::getName)
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // ✅ 2️⃣ Aggiunge anche il ruolo con prefisso "ROLE_"
        userResponse.getUser().getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });

        // ✅ 3️⃣ Crea lo UserDetails con email come username
        return org.springframework.security.core.userdetails.User.builder()
                .username(userResponse.getUser().getEmail())
                .password(userResponse.getUser().getPassword())
                .authorities(authorities)
                .accountLocked(!userResponse.getUser().getActive())
                .build();
	}
}
