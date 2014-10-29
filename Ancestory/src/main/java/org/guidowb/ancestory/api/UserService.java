package org.guidowb.ancestory.api;

import org.guidowb.ancestory.security.User;
import org.guidowb.ancestory.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		if (user == null) ; // TODO - throw not found
		user.update(update);
		validateUser(user);
		users.save(user);
	}

	@RequestMapping(value="/api/users/{username}", method=RequestMethod.DELETE)
	public void deleteUser(@PathVariable String username) {
		User user = users.findByUsername(username);
		if (user == null) ; // TODO - throw not found
		users.delete(user);
	}

	private void validateUser(User user) {
		if (user.getUsername() == null) {
			// TODO - Throw appropriate exception
		}
		if (user.getUsername().isEmpty()) {
			// TODO - Throw appropriate exception
		}
		if (!user.hasPassword()) {
			// TODO - Throw appropriate exception
		}
	}
}
