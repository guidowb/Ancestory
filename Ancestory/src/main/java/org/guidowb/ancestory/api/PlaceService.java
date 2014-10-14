package org.guidowb.ancestory.api;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.decorators.Place;
import org.guidowb.gedcom.indices.PlaceIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceService {

	@Autowired
	private Gedcom gedcom;

	@RequestMapping(value="/api/places", method=RequestMethod.GET)
	public Iterable<Place> listPlaces() {
		return gedcom.getIndex(PlaceIndex.class).places();
	}
}
