package com.example.data.model

import androidx.compose.ui.graphics.Color

/**
 * Model representing a bus stop in Fianarantsoa
 */
data class BusStop(
    val id: String,
    val name: String,
    val zone: String,
    val isHub: Boolean = false,
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

/**
 * Model representing an urban bus line (Taxi-be) in Fianarantsoa
 */
data class BusLine(
    val id: String,
    val number: String,
    val name: String,
    val departure: String,
    val terminus: String,
    val color: Color,
    val stops: List<String>,
    val landmarks: List<String>,
    val frequencyMinutes: String,
    val operatingHours: String,
    val fareAriary: Int = 500,
    val description: String,
    val isPopularForStudents: Boolean = false
)

/**
 * Route calculation result between two stops
 */
data class ItineraryStep(
    val lineId: String,
    val lineNumber: String,
    val lineColor: Color,
    val fromStop: String,
    val toStop: String,
    val stopsCount: Int,
    val estimatedMinutes: Int
)

data class ItineraryPlan(
    val isDirect: Boolean,
    val departureStop: String,
    val arrivalStop: String,
    val steps: List<ItineraryStep>,
    val transferStop: String? = null,
    val totalEstimatedMinutes: Int,
    val totalFareAriary: Int,
    val summary: String
)

/**
 * Malagasy transit phrase for the user guide
 */
data class TransitVocabulary(
    val malagasy: String,
    val phonetic: String,
    val french: String,
    val context: String,
    val audioHint: String = ""
)

/**
 * Node for the interactive visual transit schematic map
 */
data class NetworkNode(
    val stopName: String,
    val relativeX: Float, // 0.0f to 1.0f on canvas
    val relativeY: Float, // 0.0f to 1.0f on canvas
    val isMajorHub: Boolean,
    val lineNumbers: List<String>
)
