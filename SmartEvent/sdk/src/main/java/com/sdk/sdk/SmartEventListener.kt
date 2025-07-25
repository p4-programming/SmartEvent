package com.sdk.sdk

interface SmartEventListener {
    fun onEventStored(eventId: String)
    fun onFlushCompleted(successCount: Int, failedCount: Int)
}
