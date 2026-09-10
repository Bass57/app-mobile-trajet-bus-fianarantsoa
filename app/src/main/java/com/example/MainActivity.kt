package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.datasource.FianarBusData
import com.example.ui.components.BusLineCard
import com.example.ui.components.BusLineDetailModal
import com.example.ui.components.FavoritesNotesView
import com.example.ui.components.GuideAndFareView
import com.example.ui.components.ItineraryFinderView
import com.example.ui.components.NetworkMapView
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.BusViewModel
import com.example.ui.viewmodel.LineCategoryFilter

class MainActivity : ComponentActivity() {

    private val viewModel: BusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BusFianarApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BusFianarApp(viewModel: BusViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val notes by viewModel.tripNotes.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.selectedTab == AppTab.LINES,
                    onClick = { viewModel.setSelectedTab(AppTab.LINES) },
                    icon = { Icon(Icons.Default.DirectionsBus, contentDescription = "Lignes") },
                    label = { Text("Lignes", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_item_lines")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == AppTab.ITINERARY,
                    onClick = { viewModel.setSelectedTab(AppTab.ITINERARY) },
                    icon = { Icon(Icons.Default.AltRoute, contentDescription = "Itinéraire") },
                    label = { Text("Itinéraire", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_item_itinerary")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == AppTab.MAP,
                    onClick = { viewModel.setSelectedTab(AppTab.MAP) },
                    icon = { Icon(Icons.Default.Map, contentDescription = "Plan") },
                    label = { Text("Plan", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_item_map")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == AppTab.FAVORITES,
                    onClick = { viewModel.setSelectedTab(AppTab.FAVORITES) },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Favoris") },
                    label = { Text("Favoris", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_item_favorites")
                )
                NavigationBarItem(
                    selected = uiState.selectedTab == AppTab.GUIDE,
                    onClick = { viewModel.setSelectedTab(AppTab.GUIDE) },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Guide") },
                    label = { Text("Guide", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_item_guide")
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Header Top Bar
            HeaderHeroBanner()

            // Tab Content
            when (uiState.selectedTab) {
                AppTab.LINES -> {
                    LinesScreen(
                        viewModel = viewModel,
                        uiState = uiState,
                        favorites = favorites
                    )
                }
                AppTab.ITINERARY -> {
                    ItineraryFinderView(
                        departureStop = uiState.departureStop,
                        arrivalStop = uiState.arrivalStop,
                        results = uiState.itineraryResults,
                        onSelectDeparture = { viewModel.setDepartureStop(it) },
                        onSelectArrival = { viewModel.setArrivalStop(it) },
                        onSwapStops = { viewModel.swapItineraryStops() },
                        onCalculate = { viewModel.searchItinerary() }
                    )
                }
                AppTab.MAP -> {
                    NetworkMapView(
                        onSelectDeparture = { stopName ->
                            viewModel.setDepartureStop(stopName)
                            viewModel.setSelectedTab(AppTab.ITINERARY)
                        },
                        onSelectArrival = { stopName ->
                            viewModel.setArrivalStop(stopName)
                            viewModel.setSelectedTab(AppTab.ITINERARY)
                        }
                    )
                }
                AppTab.FAVORITES -> {
                    FavoritesNotesView(
                        favorites = favorites,
                        notes = notes,
                        onSelectLine = { line -> viewModel.selectLine(line) },
                        onDeleteFavorite = { fav ->
                            if (fav.type == "LINE") {
                                FianarBusData.LINES.find { it.id == fav.itemId }?.let {
                                    viewModel.toggleFavoriteLine(it)
                                }
                            } else {
                                FianarBusData.findStopByName(fav.itemId)?.let {
                                    viewModel.toggleFavoriteStop(it)
                                }
                            }
                        },
                        onDeleteNote = { id -> viewModel.deleteNote(id) },
                        isAddNoteDialogOpen = uiState.isAddNoteDialogOpen,
                        noteTitle = uiState.noteTitleInput,
                        noteStopOrLine = uiState.noteStopOrLineInput,
                        noteText = uiState.noteTextInput,
                        onOpenAddNoteDialog = { viewModel.openAddNoteDialog() },
                        onCloseAddNoteDialog = { viewModel.closeAddNoteDialog() },
                        onUpdateNoteTitle = { viewModel.updateNoteTitle(it) },
                        onUpdateNoteStopOrLine = { viewModel.updateNoteStopOrLine(it) },
                        onUpdateNoteText = { viewModel.updateNoteText(it) },
                        onSaveNote = { viewModel.saveNote() }
                    )
                }
                AppTab.GUIDE -> {
                    GuideAndFareView(
                        tripsPerDay = uiState.tripsPerDay,
                        daysPerMonth = uiState.daysPerMonth,
                        onTripsPerDayChange = { viewModel.setTripsPerDay(it) },
                        onDaysPerMonthChange = { viewModel.setDaysPerMonth(it) }
                    )
                }
            }
        }

        // Detailed Modal Bottom Sheet for inspecting a line
        uiState.selectedLine?.let { line ->
            val isFav = favorites.any { it.itemId == line.id && it.type == "LINE" }
            BusLineDetailModal(
                line = line,
                isFavorite = isFav,
                isReverseDirection = uiState.isReverseDirection,
                onToggleDirection = { viewModel.toggleDirection() },
                onToggleFavorite = { viewModel.toggleFavoriteLine(line) },
                onDismiss = { viewModel.selectLine(null) },
                onSelectStopAsDeparture = { stop ->
                    viewModel.setDepartureStop(stop)
                    viewModel.selectLine(null)
                    viewModel.setSelectedTab(AppTab.ITINERARY)
                },
                onSelectStopAsArrival = { stop ->
                    viewModel.setArrivalStop(stop)
                    viewModel.selectLine(null)
                    viewModel.setSelectedTab(AppTab.ITINERARY)
                }
            )
        }
    }
}

@Composable
fun HeaderHeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        // Hero Background Image
        Image(
            painter = painterResource(id = R.drawable.img_fianar_transit),
            contentDescription = "Fianarantsoa Bus Cityscape",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for high text contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xBB003D33),
                            Color(0xEE00251A)
                        )
                    )
                )
        )

        // Text & Badge Overlay
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF57F17)
                ) {
                    Text(
                        text = "TAXIS-BE FIANAR",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lignes Bus Fianarantsoa",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "10 lignes urbaines • 500 Ariary • Réseau 100% hors-ligne",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB2DFDB)
                )
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsBus,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun LinesScreen(
    viewModel: BusViewModel,
    uiState: com.example.ui.viewmodel.BusUiState,
    favorites: List<com.example.data.local.FavoriteItem>
) {
    val filteredLines = viewModel.getFilteredLines()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Search bar
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Rechercher ligne, arrêt (ex: Andrainjato, Zoma...)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Rechercher",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Effacer"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bus_search_field")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Chips Row (Scrollable horizontally)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LineCategoryFilter.values().forEach { category ->
                val isSelected = uiState.categoryFilter == category
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setCategoryFilter(category) },
                    label = {
                        Text(
                            text = category.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("filter_chip_${category.name.lowercase()}")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Lines List or Empty Search
        if (filteredLines.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBus,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aucune ligne de bus ne correspond à votre recherche",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredLines, key = { it.id }) { line ->
                    val isFav = favorites.any { it.itemId == line.id && it.type == "LINE" }
                    BusLineCard(
                        line = line,
                        isFavorite = isFav,
                        onToggleFavorite = { viewModel.toggleFavoriteLine(line) },
                        onClick = { viewModel.selectLine(line) }
                    )
                }
            }
        }
    }
}
