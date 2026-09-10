package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.FianarBusData
import com.example.data.local.BusDatabase
import com.example.data.local.FavoriteItem
import com.example.data.local.TripNote
import com.example.data.model.BusLine
import com.example.data.model.BusStop
import com.example.data.model.ItineraryPlan
import com.example.data.repository.BusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
    LINES("Lignes"),
    ITINERARY("Itinéraire"),
    MAP("Plan"),
    FAVORITES("Favoris"),
    GUIDE("Guide & Tarifs")
}

enum class LineCategoryFilter(val label: String) {
    ALL("Toutes les lignes"),
    STUDENTS("Campus Andrainjato"),
    MARKET("Marché Zoma"),
    HOSPITALS("Santé / CHU"),
    TRAIN_STATION("Gare FCE")
}

data class BusUiState(
    val selectedTab: AppTab = AppTab.LINES,
    val searchQuery: String = "",
    val categoryFilter: LineCategoryFilter = LineCategoryFilter.ALL,
    val selectedLine: BusLine? = null,
    val selectedStop: BusStop? = null,
    val isReverseDirection: Boolean = false,
    // Itinerary state
    val departureStop: String = "Tsianolondroa",
    val arrivalStop: String = "Andrainjato Université",
    val itineraryResults: List<ItineraryPlan> = emptyList(),
    val hasSearchedItinerary: Boolean = false,
    // Fare calculator
    val tripsPerDay: Int = 2,
    val daysPerMonth: Int = 22,
    // Note creation dialog
    val isAddNoteDialogOpen: Boolean = false,
    val noteTitleInput: String = "",
    val noteStopOrLineInput: String = "",
    val noteTextInput: String = "",
    // Message or notification banner
    val snackbarMessage: String? = null
)

class BusViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BusRepository

    private val _uiState = MutableStateFlow(BusUiState())
    val uiState: StateFlow<BusUiState> = _uiState.asStateFlow()

    init {
        val database = BusDatabase.getDatabase(application)
        repository = BusRepository(database.busDao())

        // Initial default calculation for Tsianolondroa -> Andrainjato
        searchItinerary()
    }

    val favorites: StateFlow<List<FavoriteItem>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tripNotes: StateFlow<List<TripNote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedTab(tab: AppTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setCategoryFilter(filter: LineCategoryFilter) {
        _uiState.value = _uiState.value.copy(categoryFilter = filter)
    }

    fun selectLine(line: BusLine?) {
        _uiState.value = _uiState.value.copy(
            selectedLine = line,
            isReverseDirection = false
        )
    }

    fun toggleDirection() {
        _uiState.value = _uiState.value.copy(
            isReverseDirection = !_uiState.value.isReverseDirection
        )
    }

    fun selectStop(stop: BusStop?) {
        _uiState.value = _uiState.value.copy(selectedStop = stop)
    }

    fun setDepartureStop(stop: String) {
        _uiState.value = _uiState.value.copy(departureStop = stop)
        searchItinerary()
    }

    fun setArrivalStop(stop: String) {
        _uiState.value = _uiState.value.copy(arrivalStop = stop)
        searchItinerary()
    }

    fun swapItineraryStops() {
        val current = _uiState.value
        _uiState.value = current.copy(
            departureStop = current.arrivalStop,
            arrivalStop = current.departureStop
        )
        searchItinerary()
    }

    fun searchItinerary() {
        val departure = _uiState.value.departureStop
        val arrival = _uiState.value.arrivalStop
        val results = repository.findItineraries(departure, arrival)
        _uiState.value = _uiState.value.copy(
            itineraryResults = results,
            hasSearchedItinerary = true
        )
    }

    fun toggleFavoriteLine(line: BusLine) {
        viewModelScope.launch {
            val isFav = favorites.value.any { it.itemId == line.id && it.type == "LINE" }
            repository.toggleFavorite(
                itemId = line.id,
                type = "LINE",
                title = "${line.number} - ${line.name}",
                subtitle = "${line.departure} ⇄ ${line.terminus} (${line.fareAriary} Ar)",
                isCurrentlyFav = isFav
            )
            _uiState.value = _uiState.value.copy(
                snackbarMessage = if (isFav) "${line.number} retirée des favoris" else "${line.number} ajoutée aux favoris"
            )
        }
    }

    fun toggleFavoriteStop(stop: BusStop) {
        viewModelScope.launch {
            val isFav = favorites.value.any { it.itemId == stop.name && it.type == "STOP" }
            repository.toggleFavorite(
                itemId = stop.name,
                type = "STOP",
                title = "Arrêt ${stop.name}",
                subtitle = "Zone ${stop.zone}",
                isCurrentlyFav = isFav
            )
            _uiState.value = _uiState.value.copy(
                snackbarMessage = if (isFav) "Arrêt retiré des favoris" else "Arrêt ajouté aux favoris"
            )
        }
    }

    fun setTripsPerDay(trips: Int) {
        _uiState.value = _uiState.value.copy(tripsPerDay = trips.coerceIn(1, 10))
    }

    fun setDaysPerMonth(days: Int) {
        _uiState.value = _uiState.value.copy(daysPerMonth = days.coerceIn(1, 31))
    }

    // Notes
    fun openAddNoteDialog(prefilledStopOrLine: String = "") {
        _uiState.value = _uiState.value.copy(
            isAddNoteDialogOpen = true,
            noteTitleInput = "",
            noteStopOrLineInput = prefilledStopOrLine,
            noteTextInput = ""
        )
    }

    fun closeAddNoteDialog() {
        _uiState.value = _uiState.value.copy(isAddNoteDialogOpen = false)
    }

    fun updateNoteTitle(title: String) {
        _uiState.value = _uiState.value.copy(noteTitleInput = title)
    }

    fun updateNoteStopOrLine(lineOrStop: String) {
        _uiState.value = _uiState.value.copy(noteStopOrLineInput = lineOrStop)
    }

    fun updateNoteText(text: String) {
        _uiState.value = _uiState.value.copy(noteTextInput = text)
    }

    fun saveNote() {
        val title = _uiState.value.noteTitleInput.trim()
        val lineOrStop = _uiState.value.noteStopOrLineInput.trim()
        val text = _uiState.value.noteTextInput.trim()
        if (title.isNotEmpty()) {
            viewModelScope.launch {
                repository.addTripNote(title, lineOrStop, text)
                closeAddNoteDialog()
                _uiState.value = _uiState.value.copy(snackbarMessage = "Note de trajet enregistrée !")
            }
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            repository.deleteTripNote(id)
            _uiState.value = _uiState.value.copy(snackbarMessage = "Note supprimée")
        }
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }

    fun getFilteredLines(): List<BusLine> {
        val query = _uiState.value.searchQuery.trim().lowercase()
        val category = _uiState.value.categoryFilter
        val all = repository.getAllLines()

        return all.filter { line ->
            // Category match
            val matchesCategory = when (category) {
                LineCategoryFilter.ALL -> true
                LineCategoryFilter.STUDENTS -> line.stops.any { it.contains("Andrainjato", ignoreCase = true) || it.contains("Kianjasoa", ignoreCase = true) }
                LineCategoryFilter.MARKET -> line.stops.any { it.contains("Tsianolondroa", ignoreCase = true) }
                LineCategoryFilter.HOSPITALS -> line.stops.any { it.contains("Tambohobe", ignoreCase = true) || it.contains("Hopitaly", ignoreCase = true) }
                LineCategoryFilter.TRAIN_STATION -> line.stops.any { it.contains("Gare FCE", ignoreCase = true) }
            }

            // Search query match
            val matchesQuery = if (query.isEmpty()) {
                true
            } else {
                line.number.lowercase().contains(query) ||
                    line.name.lowercase().contains(query) ||
                    line.stops.any { it.lowercase().contains(query) } ||
                    line.landmarks.any { it.lowercase().contains(query) } ||
                    line.description.lowercase().contains(query)
            }

            matchesCategory && matchesQuery
        }
    }

    fun getLinesPassing(stopName: String): List<BusLine> {
        return FianarBusData.findLinesPassingByStop(stopName)
    }
}
