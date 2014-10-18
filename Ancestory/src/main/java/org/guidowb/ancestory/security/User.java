package org.guidowb.ancestory.security;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

	private @Id @GeneratedValue long id;
	private String username = null;
	private String password = null;

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		if (password == null || password.isEmpty()) return;
		this.password = Password.hashPassword(password);
	}
	
	public boolean isPassword(String password) {
		return Password.isPassword(password, this.password);
	}
	
	public boolean hasPassword() {
		return password != null;
	}
	
	public void update(User user) {
		this.setUsername(user.username);
		this.setPassword(user.password);
	}
}
