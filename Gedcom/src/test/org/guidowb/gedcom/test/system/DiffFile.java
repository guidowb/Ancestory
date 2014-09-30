package org.guidowb.gedcom.test.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.guidowb.gedcom.GedcomWriter;

public class DiffFile {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Usage: DiffFile <gedfile>");
			System.exit(1);
		}
		final String filename = args[0];
		Gedcom gedcom = GedcomReader.load(filename);
		
		GedcomWriter.save(gedcom, new OutputStreamWriter(System.out) {

			private int lineno = 0;
			private BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));

			@Override
			public void write(String targetLine) throws IOException {
				++lineno;
				targetLine = targetLine.replaceAll("\n",  "");
				String sourceLine = reader.readLine();
				if (sourceLine.equals(targetLine)) return;
				System.err.println("Line " + Integer.toString(lineno) + " differs");
				System.err.println("> " + sourceLine + " [" + Integer.toString(sourceLine.length()) + "]");
				sourceLine = reader.readLine();
				if (sourceLine != null) System.err.println("> " + sourceLine + " [" + Integer.toString(sourceLine.length()) + "]");
				System.err.println("< " + targetLine + " [" + Integer.toString(targetLine.length()) + "]");
				System.exit(1);
			}
			
			@Override
			public void close() throws IOException {
				if (reader.readLine() != null) {
					System.err.println("Source has more lines than target");
				}
				reader.close();
			}
		});

		System.out.println("Output is identical to input");
	}
}
