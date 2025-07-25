# SmartEvent SDK - Design Document

## Persistence
Uses SQLite (no Room) with a lightweight DAO to insert, read, and update event data.

## Thread Safety
All DB operations are synchronized using a single-threaded SQLite helper.
Flush operation wraps updates in transactions.

## Safe Integration
- `SmartEvent.initialize()` must be called once.
- Optional: `setEventFilter()` and `setListener()`
- Events persist offline, so no event is lost.
