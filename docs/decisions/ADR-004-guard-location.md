# ADR-004: Guard Live Location

## Status

Accepted


## Decision

Live guard coordinates will primarily be stored in Redis GEO.


## Persistent Data

Guard profiles and emergency history remain in PostgreSQL.


## Reason

Guard coordinates change frequently and require fast geospatial queries.


## Workflow

Student sends SOS

    ->

Emergency Service

    ->

Redis GEO search

    ->

Nearby guards

    ->

Filter AVAILABLE guards

    ->

Atomically reserve nearest available guard

    ->

Assign guard