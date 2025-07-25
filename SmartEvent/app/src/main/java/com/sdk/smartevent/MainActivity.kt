package com.sdk.smartevent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.sdk.sdk.SmartEvent
import com.sdk.sdk.SmartEventListener

class MainActivity : AppCompatActivity(), SmartEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SmartEvent.initialize(this)
        SmartEvent.setListener(this)

        SmartEvent.setEventFilter { name, _ -> name != "debug_event" }

        findViewById<Button>(R.id.btnLog).setOnClickListener {
            SmartEvent.log("user_click", mapOf("screen" to "main"))
        }

        findViewById<Button>(R.id.btnFlush).setOnClickListener {
            SmartEvent.flush()
        }
    }

    override fun onEventStored(eventId: String) {
        Toast.makeText(this, "Stored: $eventId", Toast.LENGTH_SHORT).show()
    }

    override fun onFlushCompleted(successCount: Int, failedCount: Int) {
        Toast.makeText(this, "Flushed: $successCount success, $failedCount failed", Toast.LENGTH_SHORT).show()
    }
}
