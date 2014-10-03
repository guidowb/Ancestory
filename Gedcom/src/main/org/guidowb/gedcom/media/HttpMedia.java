package org.guidowb.gedcom.media;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HttpMedia implements Media {

	protected URL translateURL(URL url) throws MalformedURLException { return url; }

	@Override
	public String getCacheName(URL url) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(url.toString().getBytes());
			String cacheName = "";
			for (byte b : digest.digest()) {
				cacheName += String.format("%2x", (int) b);
			}
			return cacheName;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public URLConnection open(String url) throws MalformedURLException, IOException, URISyntaxException { return open(new URL(url)); }
	@Override
	public URLConnection open(URL url) throws IOException, URISyntaxException {
		url = translateURL(url);
		URLConnection connection = url.openConnection();
		String location = connection.getHeaderField("Location");
		if (location != null) return open(location);
		return connection;
	}
}
