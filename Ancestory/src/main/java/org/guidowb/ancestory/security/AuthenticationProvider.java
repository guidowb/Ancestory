package org.guidowb.ancestory.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

	private static final String DEFAULT_USERNAME = "admin";
	private static final String DEFAULT_PASSWORD = "admin";

	@Autowired
	private UserRepository users;

	private boolean validateUsernamePassword(String username, String password) {
		User user = users.findByUsername(username);
		if (user != null) return user.isPassword(password);
		if (username.equalsIgnoreCase(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD)) {
			// Allow default admin login only as long as there are no actual users
			// in the database.
			// TODO - Limit the granted authority to only create a new admin
			if (users.count() == 0) return true;
		}
		return false;
	}

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials().toString();
 
        if (validateUsernamePassword(username, password)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
	}

	@Override
	public boolean supports(Class<?> authClass) {
		return authClass.equals(UsernamePasswordAuthenticationToken.class);
	}

}
