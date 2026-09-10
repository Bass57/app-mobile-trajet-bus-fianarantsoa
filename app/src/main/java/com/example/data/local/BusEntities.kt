package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemId: String,
    val type: String, // "LINE" or "STOP"
    val title: String,
    val subtitle: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "trip_notes")
data class TripNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val lineOrStop: String,
    val noteText: String,
    val timestamp: Long = System.currentTimeMillis()
)
