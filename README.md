Ancestory*
==========

This is a hobby project to parse GEDCOM files and analyze/display genealogical information. It is also an
opportunity for me to experiment with Java Spring in combination with Angular.js and Bootstrap, technologies
I'm using in a current project at work. Don't expect to find anything revolutionary here, but if you stumble
across it and find a use for it, good for you.

The project consists of three components:
- A library to parse, index, and analyze genealogical data imported from a GEDCOM file
- A set of web services implemented in Java on the Spring framework, to provide a RESTful API to the
  genealogical data
- A client-side MFC Web UI implemented using Angular.js and Bootstrap

Although the Web UI is client-side, I wanted to use Spring Security, and I found that some of its features
(like CSRF protection) require server-side insertion of data into the otherwise static content. So I do make
minimal use of the Spring MVC Framework, with Thymeleaf as the template engine, and this also conveniently
eliminated the need for a separate web server to serve up the static content.


*No, this is not an accidental misspelling.


GEDCOM Library
==============

This library reads GEDCOM files and provides minimal parsing to help me extract the data I'm interested in.
The core part of it (in package org.guidowb.gedcom) is a very generic GEDCOM parser, deliberately written to
preserve the original input data, and allowing it to be written back without any loss of data.

The parser has an extensible mechanism to add indices and decorators to each record, that allows me to
incrementally add logic to extract and analyze data that I'm interested in, without complicating the core
engine or creating dependencies between these. I've been very happy with the way this is structured and found
that each of my classes remains remarkably simple.

Some of the indices and decorators I've added (in packages org.guidowb.gedcom.indices and ...gedcom.decorators)
are also completely generic, but others make some assumptions about how the data is represented in the GEDCOM
file - sometimes determined by the application that generated the file (I use ancestry.com), sometimes
reflecting conventions I use when entering the data. So if you're looking to re-use these, you may find that
they don't handle some of your data well.

Indices
-------

Here's the set of indices I have to date:

**SourceIndex** - Keeps track of the records in source file order. Mostly used to build other indices, and to
write back the file exactly as it was read. This index is fully generic.

**HierarchyIndex** - This index decorates each record to allow traversal of the GEDCOM file hierarchy (fields
and the records to which they belong, not family relationships). It's fully generic.

**RecordIndex** - Indexes records by their GEDCOM id, if they have one. Also has support for resolving cross-
references between records. It's fully generic.

**RootRecordIndex** - This index allows you to find all level 0 records by tag (after which you can enumerate
their fields through decorators)

**TagIndex** - Finds all records with a certain tag. Mostly used to build other indices. It's fully generic.

**NameIndex** - This index allows you to enumerate and find individuals by their names. It includes full support
for parsing names into their components, proper sorting of names, and aliases. Most of this is generic, but
it makes a couple of assumptions about how data is entered (nicknames in parens, lastname aliases in parens).
It also does not yet fully support name component types in the GEDCOM standard (since I don't use them).

**PlaceIndex** - Indexes all places referenced in the GEDCOM file, and tracks the events that are associated
with each place. Place names are normalized and sorted in canonical order, largest geography being the most
significant. You should find this completely generic as long as you follow the Ancestry.com convention for
naming places: Smallest geographic unit first, elements comma-separated.
