# cross-map

Cross-map is an implementation of a Persistent map and corresponding
Transient map for both Clojure and ClojureScript.

It is designed specifically for maps with keys that are [row col]
pairs, and which will typically represent sparse matrices (although it
represents dense matrices just as well).  Despite this, it supports
all types of keys -- it's just that only [row col] pairs will be
cross-indexed.  In most cases it can act just like any other Clojure map
(with some small overhead for cross-indexing).

The cross-map is designed such that any key of the form [row col] will
be cross-referenced in a row-index as well as a column-index.  This
way, accessing specific rows and columns can be done efficiently,
without the need for O(n) filter operations.  They will typically
be O(r) or O(c), where r and c are the numbers of rows and columns
respectively.

## Cross-indexing

As an example, take the following map:

(def c-map (cross-map [:a 1] :A1, [:a 2] :A2, [:b 1] :B1, [:a 3] :A3, [:c 1] :C1))
=> {[:a 1] :A1, [:a 2] :A2, [:b 1] :B1, [:a 3] :A3, [:c 1] :C1}

As you can see, it looks like any other persistent map.  However,
a this cross-map will have the following row index and column index:

(.rowIdx c-map) => {:a {1 :A1, 2 :A2, 3 :A3}, :b {1 :B1}, :c {1 :C1}}
(.colIdx c-map) => {1 {:a :A1, :b :B1, :c :C1}, 2 {:a :A2}, 3 {:a :A3}}

Removing, adding, and/or updating elements in the main map will be
mirrored in both the row-index and the col-index.

For instance, let's associate a new entry:
(def c-map2 (assoc c-map [:c 3] :C3))
=> {[:a 1] :A1, [:a 2] :A2, [:b 1] :B1, [:a 3] :A3, [:c 1] :C1, [:c 3] :C3}

We get what we would expect from any associative map.  But the row and
column indies are also updated:

(.rowIdx c-map2) => {:a {1 :A1, 2 :A2, 3 :A3}, :b {1 :B1}, :c {1 :C1, 3 :C3}}
(.colIdx c-map2) => {1 {:a :A1, :b :B1, :c :C1}, 2 {:a :A2}, 3 {:a :A3, :c :C3}}

Internally, this is achieved by maintaining three maps - the main map, the row
index and the column index.  They are updated something like this:

(assoc mainMap [:c 3] :C3)
(assoc-in rowIdx [:c 3] :C3)
(assoc-in colIdx [3 :c] :C3)

## The Cross operations

The cross-indexing enables iteration over associative maps that represent entire
rows or columns, without the overhead of iterating through the map, filtering,
and building up an intermediate structure.

Three operations are supported:
* cross
  * args: [row-keys col-keys & opts]
  * Returns a lazy sequence of [[r c] val] entries that match specifications.
* cross-rows
  * args: [row-keys & opts]
  * Returns a lazy sequence of [c column-map] entries, where column-map represents
    the entire column c, and these entries match specifications.
* cross-cols
  * args: [col-keys & opts]
  * Returns a lazy sequence of [r row-map] entries, where column-map represents
    the entire column r, and these entries match specifications.

Options are as follows:
* :any-row
  * Columns returned will have an entry in at least one of the specified rows.
    (Not valid in cross-cols)
* :every-row
  * Columns returned will have entires in all of the specified rows.
    (Not valid in cross-cols)
* :any-col
 * Rows returned will have an entry in at least one of the specified columns.
    (Not valid in cross-rows)
* :every-col
  * Rows returned will have entries in all of the specified columns.
    (Not valid in cross-rows)
* :keys-only
  * Return only the keys that match.  Keys are [r c] for cross, c for cross-rows
    and r for cross-cols.
* :vals-only
 * Return only the values that match.  Values are individual entries for cross,
    entire columns for cross-rows, and entire rows for cross-cols.
* :by-rows
 * The sequence is ordered row-first.  (Only valid in cross) 
* :by-cols
 * The sequence is ordered column-first. (Only valid in cross)

Default options, if no options are specified:
* :every-row and :every-col over :any-row and :any-col
* Neither :keys-only or :vals-only.  Key-value pairs are returned.
* :by-rows over :by-cols

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
