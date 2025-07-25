package com.sdk.sdk

import android.content.Context
import java.util.UUID


object SmartEvent {
    private var eventDao: EventDao? = null
    private var eventFilter: ((String, Map<String, Any>?) -> Boolean)? = null
    private var listener: SmartEventListener? = null
    private var eventUploader = MockServerUploader()

    fun initialize(context: Context) {
        eventDao = EventDao(context)
    }

    fun setEventFilter(filter: (String, Map<String, Any>?) -> Boolean) {
        eventFilter = filter
    }

    fun setListener(sdkListener: SmartEventListener) {
        listener = sdkListener
    }

    fun log(eventName: String, properties: Map<String, Any>?) {
        if (eventFilter?.invoke(eventName, properties) == false) return
        val event = EventEntity(
            id = UUID.randomUUID().toString(),
            name = eventName,
            timestamp = System.currentTimeMillis(),
            properties = properties?.toString() ?: "",
            synced = false
        )
        eventDao?.insert(event)
        listener?.onEventStored(event.id)
    }

    fun flush() {
        val unsyncedEvents = eventDao?.getUnsyncedEvents() ?: emptyList()
        val (success, fail) = eventUploader.upload(unsyncedEvents)

        eventDao?.markEventsAsSynced(success.map { it.id })
        listener?.onFlushCompleted(success.size, fail.size)
    }
}
