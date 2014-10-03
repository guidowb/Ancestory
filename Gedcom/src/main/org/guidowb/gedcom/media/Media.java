package org.guidowb.gedcom.media;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public interface Media {
	public abstract String getCacheName(URL url);
	public abstract URLConnection open(String url) throws MalformedURLException, IOException, URISyntaxException;
	public abstract URLConnection open(URL url) throws IOException, URISyntaxException;

}