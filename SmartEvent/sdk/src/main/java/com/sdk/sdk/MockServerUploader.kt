package com.sdk.sdk


class MockServerUploader {
    fun upload(events: List<EventEntity>): Pair<List<EventEntity>, List<EventEntity>> {
        val success = mutableListOf<EventEntity>()
        val fail = mutableListOf<EventEntity>()

        for (event in events) {
            if (event.name.contains("fail", ignoreCase = true)) {
                fail.add(event)
            } else {
                success.add(event)
            }
        }
        return Pair(success, fail)
    }
}
