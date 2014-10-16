package org.guidowb.ancestory.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

	private boolean validateUsernamePassword(String username, String password) {
		if (username.equals("user") && password.equals("password")) return true;
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
