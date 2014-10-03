package org.guidowb.gedcom.indices;

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

import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.media.AncestryMedia;
import org.guidowb.gedcom.media.HttpMedia;
import org.guidowb.gedcom.media.Media;
import org.guidowb.gedcom.indices.SourceIndex;

public class MediaIndex extends GedcomIndex {
	
	private static Map<String, Media> sources = new HashMap<String, Media>();
	static {
		sources.put(".ancestry.com", new AncestryMedia());
	}

	@Override
	public void addRecord(GedcomRecord record) {}	
	
	private File directory = null;

	private File getCacheDirectory() {
		if (directory != null) return directory;
		File sourceFile = getIndex(SourceIndex.class).getSource();
		File parentDirectory = sourceFile.getParentFile();
		if (parentDirectory == null) return null;
		String cacheDirectoryName = sourceFile.getName().replaceAll("\\..*$", "");
		directory = new File(parentDirectory, cacheDirectoryName);
		if (!directory.exists()) directory.mkdirs();
		return directory;
	}

	public File getFile(String url) throws IOException, URISyntaxException { return getFile(new URL(url)); }
	public File getFile(URL url) throws IOException, URISyntaxException {
		File file = getCacheFile(url);
		final String filename = file.getName();
		File[] matches = getCacheDirectory().listFiles(new FilenameFilter() {
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

	private Media getSource(URL url) {
		for (Entry<String, Media> source : sources.entrySet()) {
			if (url.getHost().endsWith(source.getKey())) return source.getValue();
		}
		return new HttpMedia();
	}

	public File getCacheFile(String url) throws MalformedURLException, URISyntaxException { return getCacheFile(new URL(url)); }
	public File getCacheFile(URL url) throws URISyntaxException {
		String filename = getSource(url).getCacheName(url);
		File cacheFile = new File(getCacheDirectory(), filename);
		return cacheFile;
	}

	public File downloadFile(String url) throws MalformedURLException, IOException, URISyntaxException { return downloadFile(new URL(url)); }
	public File downloadFile(URL url) throws IOException, URISyntaxException {
		Media source = getSource(url);
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
