package com.example.data.repository

import com.example.data.datasource.FianarBusData
import com.example.data.local.BusDao
import com.example.data.local.FavoriteItem
import com.example.data.local.TripNote
import com.example.data.model.BusLine
import com.example.data.model.BusStop
import com.example.data.model.ItineraryPlan
import com.example.data.model.ItineraryStep
import kotlinx.coroutines.flow.Flow
import kotlin.math.abs

class BusRepository(private val busDao: BusDao) {

    val allFavorites: Flow<List<FavoriteItem>> = busDao.getAllFavorites()
    val allNotes: Flow<List<TripNote>> = busDao.getAllNotes()

    fun getAllLines(): List<BusLine> = FianarBusData.LINES

    fun getAllStops(): List<BusStop> = FianarBusData.STOPS

    fun getLineById(id: String): BusLine? = FianarBusData.LINES.find { it.id == id }

    fun getStopByName(name: String): BusStop? = FianarBusData.findStopByName(name)

    fun isFavorite(itemId: String, type: String): Flow<Boolean> = busDao.isFavorite(itemId, type)

    suspend fun toggleFavorite(itemId: String, type: String, title: String, subtitle: String, isCurrentlyFav: Boolean) {
        if (isCurrentlyFav) {
            busDao.deleteFavoriteByItemId(itemId, type)
        } else {
            busDao.insertFavorite(
                FavoriteItem(
                    itemId = itemId,
                    type = type,
                    title = title,
                    subtitle = subtitle
                )
            )
        }
    }

    suspend fun addTripNote(title: String, lineOrStop: String, noteText: String) {
        busDao.insertNote(
            TripNote(
                title = title,
                lineOrStop = lineOrStop,
                noteText = noteText
            )
        )
    }

    suspend fun deleteTripNote(id: Int) {
        busDao.deleteNoteById(id)
    }

    /**
     * Finds itinerary options (direct or with 1 transfer) between two stops in Fianarantsoa
     */
    fun findItineraries(departureStop: String, arrivalStop: String): List<ItineraryPlan> {
        if (departureStop.isBlank() || arrivalStop.isBlank() || departureStop == arrivalStop) {
            return emptyList()
        }

        val results = mutableListOf<ItineraryPlan>()

        // 1. Direct lines
        val directLines = FianarBusData.LINES.filter { line ->
            val depIndex = line.stops.indexOfFirst { it.equals(departureStop, ignoreCase = true) }
            val arrIndex = line.stops.indexOfFirst { it.equals(arrivalStop, ignoreCase = true) }
            depIndex != -1 && arrIndex != -1
        }

        for (line in directLines) {
            val depIndex = line.stops.indexOfFirst { it.equals(departureStop, ignoreCase = true) }
            val arrIndex = line.stops.indexOfFirst { it.equals(arrivalStop, ignoreCase = true) }
            val stopsCount = abs(arrIndex - depIndex)
            val estimatedMin = 5 + (stopsCount * 3) // ~3 min per stop + 5 min wait

            results.add(
                ItineraryPlan(
                    isDirect = true,
                    departureStop = departureStop,
                    arrivalStop = arrivalStop,
                    steps = listOf(
                        ItineraryStep(
                            lineId = line.id,
                            lineNumber = line.number,
                            lineColor = line.color,
                            fromStop = departureStop,
                            toStop = arrivalStop,
                            stopsCount = stopsCount,
                            estimatedMinutes = estimatedMin
                        )
                    ),
                    transferStop = null,
                    totalEstimatedMinutes = estimatedMin,
                    totalFareAriary = line.fareAriary,
                    summary = "Trajet direct via ${line.number} ($stopsCount arrêts, ~${estimatedMin} min)"
                )
            )
        }

        // 2. If no direct lines or to provide alternatives, check 1-transfer connections
        val linesFromDeparture = FianarBusData.LINES.filter { line ->
            line.stops.any { it.equals(departureStop, ignoreCase = true) }
        }
        val linesToArrival = FianarBusData.LINES.filter { line ->
            line.stops.any { it.equals(arrivalStop, ignoreCase = true) }
        }

        for (firstLine in linesFromDeparture) {
            for (secondLine in linesToArrival) {
                if (firstLine.id == secondLine.id) continue

                // Find intersection stop (transfer stop)
                val commonStops = firstLine.stops.filter { stop ->
                    secondLine.stops.any { it.equals(stop, ignoreCase = true) }
                }

                for (transfer in commonStops) {
                    if (transfer.equals(departureStop, ignoreCase = true) || transfer.equals(arrivalStop, ignoreCase = true)) {
                        continue
                    }

                    val firstDepIndex = firstLine.stops.indexOfFirst { it.equals(departureStop, ignoreCase = true) }
                    val firstTransIndex = firstLine.stops.indexOfFirst { it.equals(transfer, ignoreCase = true) }
                    val secondTransIndex = secondLine.stops.indexOfFirst { it.equals(transfer, ignoreCase = true) }
                    val secondArrIndex = secondLine.stops.indexOfFirst { it.equals(arrivalStop, ignoreCase = true) }

                    val count1 = abs(firstTransIndex - firstDepIndex)
                    val count2 = abs(secondArrIndex - secondTransIndex)
                    val time1 = 5 + count1 * 3
                    val time2 = 6 + count2 * 3 // +6 min for transfer wait
                    val totalTime = time1 + time2
                    val totalStops = count1 + count2

                    results.add(
                        ItineraryPlan(
                            isDirect = false,
                            departureStop = departureStop,
                            arrivalStop = arrivalStop,
                            steps = listOf(
                                ItineraryStep(
                                    lineId = firstLine.id,
                                    lineNumber = firstLine.number,
                                    lineColor = firstLine.color,
                                    fromStop = departureStop,
                                    toStop = transfer,
                                    stopsCount = count1,
                                    estimatedMinutes = time1
                                ),
                                ItineraryStep(
                                    lineId = secondLine.id,
                                    lineNumber = secondLine.number,
                                    lineColor = secondLine.color,
                                    fromStop = transfer,
                                    toStop = arrivalStop,
                                    stopsCount = count2,
                                    estimatedMinutes = time2
                                )
                            ),
                            transferStop = transfer,
                            totalEstimatedMinutes = totalTime,
                            totalFareAriary = firstLine.fareAriary + secondLine.fareAriary,
                            summary = "Prendre ${firstLine.number} jusqu'à $transfer, puis correspondance avec ${secondLine.number}"
                        )
                    )
                    break // Keep the best transfer for this line pair
                }
            }
        }

        // Sort by directness first, then by estimated duration
        return results.sortedWith(compareBy({ !it.isDirect }, { it.totalEstimatedMinutes }))
    }
}
