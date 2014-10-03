package org.guidowb.gedcom.test.system;

import java.io.IOException;
import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.media.MediaCache;

public class DownloadMedia {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Usage: DownloadMedia <gedfile>");
			System.exit(1);
		}
		Gedcom gedcom = GedcomReader.load(args[0]);
		MediaCache media = gedcom.getMedia();
		for (GedcomRecord record : gedcom.records()) {
			if (!record.getTag().equals("FILE")) continue;
			String url = record.getValue();
			System.out.println(url);
			try {
				media.getFile(url);
			} catch (Exception e) {
				System.err.println("Failed to download " + url.toString() + ": " + e.getMessage());
			}
		}
	}

}
