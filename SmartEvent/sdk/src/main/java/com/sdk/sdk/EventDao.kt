package com.sdk.sdk

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EventDao(context: Context) {
    private val dbHelper = EventDbHelper(context)

    fun insert(event: EventEntity) {
        val db = dbHelper.writableDatabase
        val query = "INSERT INTO events (id, name, timestamp, properties, synced) VALUES (?, ?, ?, ?, ?)"
        db.execSQL(query, arrayOf(event.id, event.name, event.timestamp, event.properties, 0))
    }

    fun getUnsyncedEvents(): List<EventEntity> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM events WHERE synced = 0", null)
        val events = mutableListOf<EventEntity>()
        while (cursor.moveToNext()) {
            events.add(
                EventEntity(
                    id = cursor.getString(0),
                    name = cursor.getString(1),
                    timestamp = cursor.getLong(2),
                    properties = cursor.getString(3),
                    synced = cursor.getInt(4) == 1
                )
            )
        }
        cursor.close()
        return events
    }

    fun markEventsAsSynced(ids: List<String>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            for (id in ids) {
                db.execSQL("UPDATE events SET synced = 1 WHERE id = ?", arrayOf(id))
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    private class EventDbHelper(context: Context) : SQLiteOpenHelper(context, "smart_event.db", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE events (
                    id TEXT PRIMARY KEY,
                    name TEXT,
                    timestamp INTEGER,
                    properties TEXT,
                    synced INTEGER
                )
            """.trimIndent())
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }
}
