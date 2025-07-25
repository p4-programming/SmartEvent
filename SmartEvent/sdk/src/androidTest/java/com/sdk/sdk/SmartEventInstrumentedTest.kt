package com.sdk.sdk

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SmartEventInstrumentedTest {

    @Test
    fun testEventStorageAndFlush() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        SmartEvent.initialize(context)

        var stored = false
        var flushed = false

        SmartEvent.setListener(object : SmartEventListener {
            override fun onEventStored(eventId: String) {
                stored = true
            }

            override fun onFlushCompleted(successCount: Int, failedCount: Int) {
                flushed = true
            }
        })

        SmartEvent.setEventFilter { _, _ -> true }
        SmartEvent.log("test_event", mapOf("key" to "value"))
        SmartEvent.flush()

        assertTrue(stored)
        assertTrue(flushed)
    }
}
