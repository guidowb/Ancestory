package org.guidowb.ancestory.api;

import org.guidowb.ancestory.security.User;
import org.guidowb.ancestory.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserService {

	@Autowired
	private UserRepository users;

	@RequestMapping(value="/api/users", method=RequestMethod.GET)
	public Iterable<User> listUsers() {
		return users.findAll();
	}

	@RequestMapping(value="/api/users/{username}", method=RequestMethod.GET)
	public User getUser(@PathVariable String username) {
		User user = users.findByUsername(username);
		if (user == null) throw new UserNotFoundException();
		return user;
	}

	@RequestMapping(value="/api/users", method=RequestMethod.POST)
	public void createUser(@RequestBody User user) {
		validateUser(user);
		users.save(user);
	}

	@RequestMapping(value="/api/users/{username}", method=RequestMethod.PUT)
	public void updateUser(@PathVariable String username, @RequestBody User update) {
		User user = users.findByUsername(username);
		if (user == null) throw new UserNotFoundException();
		user.update(update);
		validateUser(user);
		users.save(user);
	}

	@RequestMapping(value="/api/users/{username}", method=RequestMethod.DELETE)
	public void deleteUser(@PathVariable String username) {
		User user = users.findByUsername(username);
		if (user == null) throw new UserNotFoundException();
		users.delete(user);
	}

	private void validateUser(User user) {
		if (user.getUsername() == null || user.getUsername().isEmpty()) throw new UsernameRequiredException();
		if (!user.hasPassword()) throw new PasswordRequiredException();
	}
	
	@SuppressWarnings("serial")
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such user")
	private class UserNotFoundException extends RuntimeException {}

	@SuppressWarnings("serial")
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Username is required")
	private class UsernameRequiredException extends RuntimeException {}

	@SuppressWarnings("serial")
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Password is required")
	private class PasswordRequiredException extends RuntimeException {}
}
