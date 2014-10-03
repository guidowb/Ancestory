package org.guidowb.gedcom.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MediaCache {
	
	private static Map<String, MediaSource> sources = new HashMap<String, MediaSource>();
	static {
		sources.put(".ancestry.com", new AncestrySource());
	}

	private File directory;
	
	public MediaCache(File directory) {
		this.directory = directory;
	}

	public File getFile(String url) throws IOException, URISyntaxException { return getFile(new URL(url)); }
	public File getFile(URL url) throws IOException, URISyntaxException {
		File file = getCacheFile(url);
		final String filename = file.getName();
		File[] matches = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				if (name.equals(filename)) return true;
				if (name.startsWith(filename + ".")) return true;
				return false;
			}			
		});
		if (matches.length > 0) return matches[0];
		return downloadFile(url);
	}

	private MediaSource getSource(URL url) {
		for (Entry<String, MediaSource> source : sources.entrySet()) {
			if (url.getHost().endsWith(source.getKey())) return source.getValue();
		}
		return new HttpSource();
	}

	public File getCacheFile(String url) throws MalformedURLException, URISyntaxException { return getCacheFile(new URL(url)); }
	public File getCacheFile(URL url) throws URISyntaxException {
		String filename = getSource(url).getCacheName(url);
		if (!directory.exists()) directory.mkdirs();
		File cacheFile = new File(directory, filename);
		return cacheFile;
	}

	public File downloadFile(String url) throws MalformedURLException, IOException, URISyntaxException { return downloadFile(new URL(url)); }
	public File downloadFile(URL url) throws IOException, URISyntaxException {
		MediaSource source = getSource(url);
		URLConnection connection = source.open(url);
		String extension = connection.getContentType().replaceAll("^.*/", "");
		InputStream input = connection.getInputStream();
		File cacheFile = getCacheFile(url);
		if (extension != null && !extension.isEmpty()) cacheFile = new File(cacheFile.getAbsolutePath() + "." + extension);
		OutputStream output = new FileOutputStream(cacheFile);
		copy(input, output);
		output.close();
		return cacheFile;
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[32 * 1024];
		int count;
		while (0 < (count = in.read(buffer))) {
			out.write(buffer, 0, count);
		}
	}
}
