# cross-map

Cross-map is an implementation of a Persistent corresponding Transient
map for both Clojure and ClojureScript.

It is designed specifically for maps with keys that are [row col]
pairs, and which will typically represent sparse matrices (although it
represents dense matrices just as well).  Despite this, it supports
all operations supported by the default Clojure maps, and in most
cases will act just like any other Clojure map (with some small
overhead).

The cross-map is designed such that any key of the form [row col] will
be cross-referenced in a row-index as well as a column-index.  This
way, accessing specific rows and columns can be done efficiently,
without the need for filter operations.

As an example, take the following map:

{[:a 1] :A1, [:a 2] :A2, [:b 1] :B1, [:a 3] :A3, [:c 1] :C1}

A cross-map representation of this map will have the following
row-index and col-index:

row-index: {:a {1 :A1, 2 :A2, 3 :A3}, :b {1 :B1}, :c {1 :C1}}
col-index: {1 {:a :A1, :b :B1, :c :C1}, 2 {:a :A2}, 3 {:a :A3}}

Removing, adding, and/or updating elements in the main map will be
mirrored in both the row-index and the col-index.

This enables iteration over associative maps that represent entire
rows or columns, without the overhead of iterating through the map,
filtering, and building up an intermediate structure.

## Motivation

I made this map implementation as the first step in the creation of
the SPECS game scripting framework (not on GitHub yet, but will soon
be).

SPECS stands for "[Sequential Processes](https://en.wikipedia.org/wiki/Communicating_sequential_processes) and [Entity Component System](https://en.wikipedia.org/wiki/Entity_component_system)".
As the name suggests, it uses the Communicating Sequential Processes
and channels from [core.async](https://github.com/clojure/core.async) as a means of controlling game state.

The game state itself is modeled with Entities and Components.  Each
entity is identified by a unique UUID, and each Component has a
component-type (metadata tags in this implementation).  System
functions provide a means of defining synchronous logic that is
implemented as part of the update cycle, as opposed to CSP
"controllers", which are asynchronous.

Both System functions and CSP channels are bound to "component
profiles".  These are specifications of which components are necessary
for that system to run.  (Binding to a subset of entity ID's will also
be supported).

For instance, an "update-velocity" system may only be executed on an
entity with both a Position component and a Velocity component.  It
should be able to ignore all the rest.  Similarly, channels will
dispatch messages based on component profiles.  In order to do this
more efficiently, there should be a way of ignoring those entities
that do not match the profile.

Rather than thinking of entities as "containers" of components,
individual components are stored in a sparse matrix where entity ID's
are the rows and component types are the columns.

By storing this matrix as a cross-map, we get the best of both
worlds.  We can easily and efficiently view entities as though they
were collections of components.  Similarly, we can view the full set
of ID's for entities that have a particular type (or types) of
component.

## Usage



## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
