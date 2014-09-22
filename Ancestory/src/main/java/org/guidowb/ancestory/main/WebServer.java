package org.guidowb.ancestory.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class WebServer {

	@RequestMapping(value="/", method=RequestMethod.GET)
	public void defaultRedirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/web/index.html");
	}
	
	@RequestMapping(value="/web/**", method=RequestMethod.GET)
	public void getStaticFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestedPath = getRequestPath(request).replaceFirst("/web/?", "");
		File requestedFile = new File("src/main/resources", requestedPath);
		if (!requestedFile.exists()) throw new FileNotFoundException(requestedPath);
		copy(new FileInputStream(requestedFile), response.getOutputStream());
	}
	
	@SuppressWarnings("serial")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public class FileNotFoundException extends RuntimeException {
		public FileNotFoundException(String fileName) {
			super("Not found: " + fileName);
		}
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[32 * 1024];
		int count;
		while (0 < (count = in.read(buffer))) {
			out.write(buffer, 0, count);
		}
	}

	private static String getRequestPath(HttpServletRequest request) {
		return (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	}
}
