package org.guidowb.gedcom.test.system;

import java.io.IOException;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomReader;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.decorators.Description;
import org.guidowb.gedcom.decorators.Place;
import org.guidowb.gedcom.indices.PlaceIndex;

public class ListPlaces {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Usage: ListPlaces <gedfile>");
			System.exit(1);
		}
		Gedcom gedcom = GedcomReader.load(args[0]);
		for (Place place : gedcom.getIndex(PlaceIndex.class).places()) {
			System.out.println(place.getName());
			for (GedcomRecord event : place.events()) {
				System.out.println("    " + Description.getDescription(event));
			}
		}
	}
}
