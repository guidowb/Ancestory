package org.guidowb.gedcom.test.system;

import java.io.IOException;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.indices.MediaIndex;
import org.guidowb.gedcom.indices.TagIndex;

public class DownloadMedia {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Usage: DownloadMedia <gedfile>");
			System.exit(1);
		}
		Gedcom gedcom = GedcomReader.load(args[0]);
		MediaIndex media = gedcom.getIndex(MediaIndex.class);
		for (GedcomRecord record : gedcom.getIndex(TagIndex.class).getRecords("FILE")) {
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
