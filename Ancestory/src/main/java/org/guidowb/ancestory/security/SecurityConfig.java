package org.guidowb.ancestory.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.csrf.CsrfToken;

@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public AuthenticationProvider authenticationProvider() {
		AuthenticationProvider provider = new AuthenticationProvider();
		return provider;
	}

	/**
	 * This filter injects AngularJS compatible cookies into the response whenever
	 * the CSRF token changes value. This enables use of Angular $http with APIs
	 * that have csrf() protection enabled, while minimizing the risk of tokens
	 * leaking.
	 * 
	 * @return
	 */
	@Bean Filter xsrfFilter() {
		return new Filter() {
			private String lastTokenValue = null;

			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
				chain.doFilter(request, response);
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpResponse = (HttpServletResponse) response;
					CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
					if (csrfToken == null) return;
					String tokenValue = csrfToken.getToken();
					String headerValue = csrfToken.getHeaderName();
					if (tokenValue.equals(lastTokenValue)) return;
					lastTokenValue = tokenValue;
					Cookie tokenCookie = new Cookie("XSRF-TOKEN", tokenValue);
					Cookie headerCookie = new Cookie("XSRF-HEADER", headerValue);
					tokenCookie.setPath("/");
					headerCookie.setPath("/");
					httpResponse.addCookie(tokenCookie);
					httpResponse.addCookie(headerCookie);
				}
			}

			@Override public void init(FilterConfig config) throws ServletException {}
			@Override public void destroy() {}
		};
	}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    		.authorizeRequests()
    			.antMatchers("/css/**").permitAll()
    			.antMatchers("/images/**").permitAll()
    			.antMatchers("/external/**").permitAll()
    			.anyRequest().authenticated()
    			.and()
    		.formLogin()
    			.loginPage("/login")
    			.permitAll()
    			.and()
    		.logout()
    			.permitAll();
    }
}