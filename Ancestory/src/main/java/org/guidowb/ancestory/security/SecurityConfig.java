package org.guidowb.ancestory.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public AuthenticationProvider authenticationProvider() {
		AuthenticationProvider provider = new AuthenticationProvider();
		return provider;
	}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
        	.authenticationProvider(authenticationProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    		.authorizeRequests()
    			.antMatchers("/css/**").permitAll()
    			.antMatchers("/images/**").permitAll()
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