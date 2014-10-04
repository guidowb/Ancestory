package org.guidowb.gedcom.indices;

import java.util.SortedMap;
import java.util.TreeMap;

import org.guidowb.gedcom.GedcomIndex;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.decorators.Place;

public class PlaceIndex extends GedcomIndex {

	SortedMap<String, Place> placesByName = new TreeMap<String, Place>();

	@Override
	public void addRecord(GedcomRecord record) {
		if (!record.getTag().equals("PLAC")) return;
		Place place = findPlace(record.getValue());
		GedcomRecord reference = record.getContainer();
		if (reference != null) place.addReference(record.getContainer());
	}

	private synchronized Place findPlace(String name) {
		name = normalize(name);
		Place place = placesByName.get(name);
		if (place != null) return place;
		place = new Place(name);
		placesByName.put(name, place);
		return place;
	}
	
	private String normalize(String name) {
		if (name == null) return null;
		String[] words = name.split(",");
		if (words.length < 1) return null;
		String normalized = words[words.length - 1].trim();
		for (int w = words.length-2; w >= 0; w--) {
			normalized += ", " + words[w].trim();
		}
		return normalized;
	}
	
	public Iterable<Place> places() { return placesByName.values(); }
}
