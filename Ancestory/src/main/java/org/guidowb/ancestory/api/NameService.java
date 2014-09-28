package org.guidowb.ancestory.api;

import org.guidowb.gedcom.Gedcom;
import org.guidowb.gedcom.decorators.Name;
import org.guidowb.gedcom.indices.NameIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NameService {

	@Autowired
	private Gedcom gedcom;

	@RequestMapping(value="/api/names", method=RequestMethod.GET)
	public Iterable<Name> listNames() {
		return gedcom.getIndex(NameIndex.class).names();
	}
}
