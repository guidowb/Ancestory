package org.guidowb.ancestory.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebServer {

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getHomePage() {
		return "redirect:/ancestory";
	}

	@RequestMapping(value="/ancestory", method=RequestMethod.GET)
	public String getMainPage() {
		return "ancestory";
	}

	@RequestMapping(value="/ancestory/**", method=RequestMethod.GET)
	public String getSubPage() {
		return "ancestory";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String getLoginPage() {
		return "login";
	}
}
