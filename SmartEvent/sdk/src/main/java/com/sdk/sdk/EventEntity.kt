package com.sdk.sdk

data class EventEntity(
    val id: String,
    val name: String,
    val timestamp: Long,
    val properties: String,
    var synced: Boolean
)
