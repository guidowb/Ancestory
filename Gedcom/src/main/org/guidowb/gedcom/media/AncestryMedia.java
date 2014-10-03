package org.guidowb.gedcom.media;

import java.net.MalformedURLException;
import java.net.URL;

public class AncestryMedia extends HttpMedia {

	private String getQueryParam(URL url, String name) {
		String query = url.getQuery();
		String[] params = query.split("&");
		for (String param : params) {
			String[] words = param.split("=", 2);
			String key = words[0];
			String value = words.length > 1 ? words[1] : null;
			if (key.equals(name)) {
				return value;
			}
		}
		return null;
	}

	@Override
	protected URL translateURL(URL url) throws MalformedURLException {
		String guid = getQueryParam(url, "guid");
		return new URL("http://mediasvc.ancestry.com/v2/image/namespaces/1093/media/" + guid + "?client=TreesUI");
	}
	
	@Override
	public String getCacheName(URL url) {
		String guid = getQueryParam(url, "guid");
		return "com.ancestry." + guid;
	}
}
