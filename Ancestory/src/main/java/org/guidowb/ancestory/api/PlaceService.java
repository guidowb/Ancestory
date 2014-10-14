package org.guidowb.ancestory.api;

import java.util.ArrayList;
import java.util.List;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.GedcomRecord;
import org.guidowb.gedcom.decorators.Description;
import org.guidowb.gedcom.decorators.Place;
import org.guidowb.gedcom.indices.PlaceIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

	public class Event {
		public String description;
	}

	@RequestMapping(value="/api/places/{placename}/events", method=RequestMethod.GET)
	public Iterable<Event> listEvents(@PathVariable String placename) {
		Place place = gedcom.getIndex(PlaceIndex.class).findPlace(placename);
		List<Event> events = new ArrayList<Event>();
		for (GedcomRecord record : place.events()) {
			Event event = new Event();
			event.description = Description.getDescription(record).toString();
			events.add(event);
		}
		return events;
	}
}
