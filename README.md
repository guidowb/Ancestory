Ancestory*
==========

This is a hobby project to parse GEDCOM files and analyze/display genealogical information. It is also an
opportunity for me to experiment with Java Spring in combination with Angular.js and Bootstrap, technologies
I'm using in a current project at work. Don't expect to find anything revolutionary here, but if you stumble
across it and find a use for it, that would be great.

The project will eventually consist of three components:
- A library to parse, index, and analyze genealogical data imported from a GEDCOM file
- A set of web services implemented in Java on the Spring framework, to provide a RESTful API to the
  genealogical data
- A client-side MFC Web UI implemented using Angular.js and Bootstrap

There is also a *very* lightweight little web server in the set of web services to serve up the static
content for the Web UI. This is just to make it easier to deploy for testing. Don't even look at it :)

As of now, the only component that is reasonably built out is the GEDCOM library. The others will receive
some more attention shortly.

*No, this is not an accidental misspelling.


GEDCOM Library
==============

This library reads GEDCOM files and provides minimal parsing to help me extract the data I'm interested in.
The first part of it (in package org.guidowb.gedcom) is a very generic GEDCOM parser, deliberately written to
preserve the original input data, and allow it to be written back without any loss of data.

The parser has an extenisble mechanism to add indices and decorations to each record, that allows me to add
indices and analyses for specific data I'm interested in. Some of these indices are also completely generic,
but others make some assumptions about how the data is represented in the GEDCOM file - sometimes determined
by the application that generated the file (I use ancestry.com), sometimes reflecting conventions I use when
entering the data.

Here's the set of indices I have to date:

HierarchyIndex - This index decorates each record to allow traversal of the GEDCOM file hierarchy (fields
and the records to which they belong, not family relationships). It's fully generic.

RootRecordIndex - This index allows you to find all level 0 records by tag (after which you can enumerate
their fields through decorators)

NameIndex - This index allows you to enumerate and find individuals by their names. It includes full support
for parsing names into their components, proper sorting of names, and aliases. Most of this is generic, but
it makes a couple of assumptions about how data is entered (nicknames in parens, lastname aliases in parens).
It also does not yet fully support name component types in the GEDCOM standard (since I don't use them).
