# GUIDE TECHNIQUE & RÉPERTOIRE EXHAUSTIF DES FONCTIONS DU PROJET
## Application Android : Bus Fianarantsoa (Madagascar)
**Stack Technologique :** Kotlin, Jetpack Compose, Material Design 3, Room Database (SQLite), Coroutines & Flow, MVVM / Clean Architecture.

---

## SOMMAIRE
1. **Architecture Globale & Organisation du Code**
2. **Répertoire Exhaustif de TOUTES les Fonctions par Fichier**
   - 2.1. Source de Données & Moteur Algorithmique (`FianarBusData.kt`)
   - 2.2. Interface d'Accès aux Données SQLite (`BusDao.kt`)
   - 2.3. Gestionnaire de Base de Données (`BusDatabase.kt`)
   - 2.4. Couche d'Orchestration & Source de Vérité (`BusRepository.kt`)
   - 2.5. Logique Métier & Contrôle d'État (`BusViewModel.kt`)
   - 2.6. Activité Principale & Structure de Navigation (`MainActivity.kt`)
   - 2.7. Composants Graphiques de Présentation (`ui/components/*`)
3. **Modèles de Données & Entités Persistantes**
4. **Algorithmes de Calcul d'Itinéraire (Directs & Correspondances)**
5. **Rendu Graphique Canvas & Interaction Tactile du Plan du Réseau**
6. **Thème Graphique, Accessibilité & Tests Automatisés**

---

## 1. ARCHITECTURE GLOBALE & FLUX DE DONNÉES

L'application respecte les principes de la **Clean Architecture** et du patron de conception **MVVM (Model - View - ViewModel)** préconisés par Google pour le développement Android moderne.

### Le Flux Unidirectionnel des Données (UDF - Unidirectional Data Flow) :
1. **L'Utilisateur** effectue une action dans l'interface Compose (ex: sélectionne un arrêt, saisit une recherche, bascule une ligne en favori).
2. **La Vue (Composable)** déclenche une méthode du `BusViewModel` (ex: `viewModel.setDepartureStop("Tsianolondroa")`).
3. **Le ViewModel** traite la requête en coroutine (`viewModelScope`), consulte ou modifie les données via `BusRepository`, et produit un nouvel état immuable `BusUiState`.
4. **Le StateFlow** (`_uiState.asStateFlow()`) diffuse le nouvel état à la Vue.
5. **Jetpack Compose** recompose uniquement les éléments de l'UI concernés grâce à `collectAsStateWithLifecycle()`.

---

## 2. RÉPERTOIRE EXHAUSTIF DE TOUTES LES FONCTIONS

### 2.1. `FianarBusData.kt` (`com.example.data.datasource`)
Ce fichier singleton (`object FianarBusData`) détient la cartographie urbaine de Fianarantsoa et les algorithmes de calcul sans dépendance réseau.

| Fonction | Paramètres | Type de retour | Rôle & Explication détaillée |
| :--- | :--- | :--- | :--- |
| `getLines()` | Aucun | `List<BusLine>` | Retourne la liste complète et immuable des 15 lignes de taxi-be authentiques de Fianarantsoa (Lignes 38, CB Collectif Barrière, 40, 30, 32, 33, 48, 21, 22, 23, 26, 28, 29, 34, 39) avec leurs arrêts, terminus, fréquences et couleurs. |
| `getStops()` | Aucun | `List<BusStop>` | Retourne l'annuaire des 35+ arrêts physiques répertoriés avec leur zone géographique et description de repérage. |
| `getNetworkNodes()` | Aucun | `List<NetworkNode>` | Fournit les coordonnées spatiales relatives (X, Y entre 0.0 et 1.0) de chaque station pour le dessin vectoriel sur le Canvas. |
| `getStudentTips()` | Aucun | `List<StudentTip>` | Fournit la liste des conseils pratiques dédiés aux étudiants desservant le campus universitaire d'Andrainjato. |
| `getVocabularyList()` | Aucun | `List<VocabularyItem>` | Fournit les fiches lexicales bilingues Malagasy/Français des expressions indispensables en Taxi-be. |
| `findStopByName(name)` | `name: String` | `BusStop?` | Recherche insensible à la casse d'un arrêt dans le catalogue. Retourne l'objet `BusStop` correspondant ou `null`. |
| `findLinesPassingByStop(stopName)` | `stopName: String` | `List<BusLine>` | Détermine et renvoie toutes les lignes de bus qui desservent l'arrêt spécifié. Essentiel pour identifier les correspondances possibles. |
| `calculateItinerary(departure, arrival)` | `departure: String, arrival: String` | `List<ItineraryPlan>` | Moteur d'itinéraire en 2 phases. Phase 1 : recherche les lignes directes communes aux deux arrêts et calcule la durée (3 min/arrêt) et le tarif exact (600 Ar standard, ou 500 Ar sur Ligne 38 et Collectif Barrière CB). Phase 2 : explore les carrefours hubs (Tsianolondroa, Ampasambazaha, Antarandolo, Beravina) pour proposer des trajets avec 1 correspondance. |

---

### 2.2. `BusDao.kt` (`com.example.data.local`)
Interface Data Access Object (DAO) de Room définissant les requêtes SQLite sécurisées.

| Fonction | Paramètres | Type de retour | Rôle & Explication détaillée |
| :--- | :--- | :--- | :--- |
| `getAllFavorites()` | Aucun | `Flow<List<FavoriteItem>>` | Requête SQL réactive (`SELECT * FROM favorites ORDER BY createdAt DESC`) émettant la liste des favoris dès qu'un changement survient dans SQLite. |
| `getFavoritesByType(type)` | `type: String` | `Flow<List<FavoriteItem>>` | Filtre les favoris enregistrés par catégorie (`LINE` pour les lignes ou `STOP` pour les arrêts). |
| `getFavoriteById(itemId, type)` | `itemId: String, type: String` | `FavoriteItem?` | Requête ponctuelle suspendue vérifiant si un identifiant spécifique est déjà enregistré en favori. |
| `insertFavorite(favorite)` | `favorite: FavoriteItem` | `Unit` (suspend) | Insère un nouvel élément favori en base SQLite, remplaçant tout enregistrement en conflit (`OnConflictStrategy.REPLACE`). |
| `deleteFavorite(favorite)` | `favorite: FavoriteItem` | `Unit` (suspend) | Supprime l'entité favori spécifiée de la table SQLite. |
| `deleteFavoriteById(itemId, type)` | `itemId: String, type: String` | `Unit` (suspend) | Supprime un favori directement via sa clé composite itemId + type. |
| `getAllNotes()` | Aucun | `Flow<List<TripNote>>` | Requête SQL réactive émettant le carnet complet des mémos de voyage de l'utilisateur, classés du plus récent au plus ancien. |
| `insertNote(note)` | `note: TripNote` | `Unit` (suspend) | Enregistre un mémo de voyage personnel dans la table `trip_notes`. |
| `deleteNoteById(id)` | `id: Int` | `Unit` (suspend) | Supprime définitivement un mémo par son identifiant primaire dans SQLite. |

---

### 2.3. `BusDatabase.kt` (`com.example.data.local`)
Classe abstraite héritant de `RoomDatabase` qui configure la persistance SQLite.

| Fonction | Paramètres | Type de retour | Rôle & Explication détaillée |
| :--- | :--- | :--- | :--- |
| `busDao()` | Aucun | `BusDao` | Méthode abstraite que Room implémente automatiquement pour donner accès aux requêtes DAO. |
| `getInstance(context)` | `context: Context` | `BusDatabase` | Implémente le patron Singleton avec double-vérification thread-safe (`@Volatile` et `synchronized(this)`). Évite la création multiple et coûteuse d'instances de la base SQLite `fianar_bus.db`. |

---

### 2.4. `BusRepository.kt` (`com.example.data.repository`)
Orchestrateur centralisant l'accès aux données. Il découple le ViewModel des mécanismes de persistance.

| Fonction | Paramètres | Type de retour | Rôle & Explication détaillée |
| :--- | :--- | :--- | :--- |
| `getAllLines()` | Aucun | `List<BusLine>` | Fournit le catalogue complet des lignes depuis la source de données. |
| `getAllStops()` | Aucun | `List<BusStop>` | Fournit l'annuaire complet des arrêts de bus. |
| `searchLines(query, category)` | `query: String, category: LineCategoryFilter` | `List<BusLine>` | Combine un filtre textuel (recherche sur le numéro, terminus ou liste des arrêts) et un filtre thématique (Université, Marché, Hôpital, Gare). |
| `getItinerary(departure, arrival)` | `departure: String, arrival: String` | `List<ItineraryPlan>` | Délègue le calcul d'itinéraire au moteur algorithmique. |
| `getAllFavorites()` | Aucun | `Flow<List<FavoriteItem>>` | Expose le flux réactif des favoris de l'utilisateur. |
| `isFavorite(itemId, type)` | `itemId: String, type: String` | `Flow<Boolean>` | Transforme le flux des favoris pour indiquer sous forme de booléen si l'élément spécifié est marqué comme favori. |
| `toggleFavorite(itemId, type, title, subtitle)` | `itemId, type, title, subtitle` | `Unit` (suspend) | Vérifie si l'élément existe déjà en base : s'il existe, il le supprime ; s'il n'existe pas, il l'insère. Offre une gestion en un seul clic. |
| `getAllNotes()` | Aucun | `Flow<List<TripNote>>` | Expose le flux réactif de tous les mémos personnels. |
| `addNote(title, lineOrStop, noteText)` | `title, lineOrStop, noteText` | `Unit` (suspend) | Construit l'objet `TripNote` avec horodatage actuel et l'insère en base SQLite via le DAO. |
| `deleteNote(id)` | `id: Int` | `Unit` (suspend) | Supprime un mémo dans la base SQLite via le DAO. |

---

### 2.5. `BusViewModel.kt` (`com.example.ui.viewmodel`)
Contrôleur principal de l'UI. Hérite d'AndroidViewModel, survit aux changements de configuration et expose un `StateFlow<BusUiState>`.

| Fonction | Paramètres | Type de retour | Rôle & Explication détaillée |
| :--- | :--- | :--- | :--- |
| `setSelectedTab(tab)` | `tab: AppTab` | `Unit` | Modifie l'onglet actif dans la barre de navigation inférieure (Lignes, Itinéraire, Plan, Favoris, Guide). |
| `setSearchQuery(query)` | `query: String` | `Unit` | Met à jour le terme de recherche textuelle dans l'état UI. |
| `setCategoryFilter(filter)` | `filter: LineCategoryFilter` | `Unit` | Active ou désactive un filtre thématique (Toutes, Université, Marché, Hôpital, Gare). |
| `getFilteredLines()` | Aucun | `List<BusLine>` | Calcule dynamiquement la liste des lignes correspondant à la recherche et à la catégorie active. |
| `selectLine(line)` | `line: BusLine?` | `Unit` | Définit la ligne à afficher dans la feuille modale de détail (`ModalBottomSheet`), ou `null` pour fermer la modale. Réinitialise le sens à 'Aller'. |
| `toggleDirection()` | Aucun | `Unit` | Inverse le sens de parcours (Aller ⇄ Retour) pour la ligne actuellement sélectionnée dans la modale. |
| `toggleFavoriteLine(line)` | `line: BusLine` | `Unit` | Ajoute ou retire la ligne des favoris et programme l'affichage d'un message de confirmation dans le Snackbar. |
| `toggleFavoriteStop(stop)` | `stop: BusStop` | `Unit` | Ajoute ou retire un arrêt de bus des favoris. |
| `setDepartureStop(stop)` | `stop: String` | `Unit` | Met à jour l'arrêt de départ sélectionné et déclenche automatiquement le calcul d'itinéraire si l'arrivée est déjà définie. |
| `setArrivalStop(stop)` | `stop: String` | `Unit` | Met à jour l'arrêt d'arrivée sélectionné et déclenche automatiquement le calcul d'itinéraire. |
| `swapItineraryStops()` | Aucun | `Unit` | Intervertit l'arrêt de départ et l'arrêt d'arrivée en un clic et recalcule immédiatement l'itinéraire inverse. |
| `searchItinerary()` | Aucun | `Unit` | Lance le calcul de trajet entre le départ et l'arrivée actuels et place les résultats dans l'état UI. |
| `openAddNoteDialog(prefilledRef)`| `prefilledRef: String` | `Unit` | Ouvre la boîte de dialogue d'enregistrement de mémo en pré-remplissant éventuellement la ligne ou l'arrêt concerné. |
| `closeAddNoteDialog()` | Aucun | `Unit` | Ferme la boîte de dialogue de mémo et réinitialise les champs de saisie. |
| `setNoteTitle(title)` | `title: String` | `Unit` | Met à jour le champ titre du mémo dans l'état UI. |
| `setNoteLineOrStop(lineOrStop)` | `lineOrStop: String` | `Unit` | Met à jour la référence de ligne ou d'arrêt dans l'état UI. |
| `setNoteText(text)` | `text: String` | `Unit` | Met à jour le corps du texte du mémo dans l'état UI. |
| `saveNote()` | Aucun | `Unit` | Valide la saisie, enregistre le mémo dans la base SQLite via une coroutine `viewModelScope`, ferme le dialogue et notifie l'usager par Snackbar. |
| `deleteNote(id)` | `id: Int` | `Unit` | Supprime le mémo spécifié dans la base SQLite. |
| `clearSnackbar()` | Aucun | `Unit` | Réinitialise le message Snackbar après affichage pour éviter les répétitions. |

---

### 2.6. `MainActivity.kt`
Point d'entrée de l'application Android et hôte de l'arborescence Compose.

| Fonction / Composable | Paramètres | Rôle & Explication détaillée |
| :--- | :--- | :--- |
| `onCreate(savedInstanceState)` | `Bundle?` | Méthode du cycle de vie Android. Active l'affichage plein écran moderne Edge-to-Edge (`enableEdgeToEdge()`) et monte le composable racine dans `setContent`. |
| `BusFianarApp(viewModel)` | `viewModel: BusViewModel` | Composable racine de l'application. Initialise le `Scaffold`, positionne la barre de navigation inférieure (`NavigationBar`), gère les marges sécurisées (`WindowInsets`) et affiche l'écran correspondant à l'onglet sélectionné. |
| `HeaderHeroBanner(currentTab)` | `currentTab: AppTab` | Affiche la bannière visuelle supérieure : image d'illustration urbaine, dégradé de protection pour le contraste, badge de localisation 'Madagascar - Haute Matsiatra' et titre contextuel. |

---

### 2.7. Composants de Présentation (`ui/components/*`)

| Composable | Fichier | Rôle & Explication détaillée |
| :--- | :--- | :--- |
| `BusLinesView` | `BusLineCard.kt` | Vue d'ensemble de l'annuaire des lignes. Comporte la barre de recherche textuelle, les filtres défilants horizontaux (`FilterChip`) et la liste verticale (`LazyColumn`) des cartes de lignes. |
| `BusLineCard` | `BusLineCard.kt` | Composant de carte Material 3 représentant une ligne : badge coloré avec son numéro/code (ex: L38, L40, CB), terminus, fréquence de passage, badge tarifaire (600 Ar standard, ou 500 Ar dérogatoire étudiant pour L38 et CB), aperçu des arrêts majeurs et bouton favori interactif. |
| `BusLineDetailModal` | `BusLineDetailModal.kt` | Feuille modale inférieure (`ModalBottomSheet`) affichant la fiche détaillée de la ligne, bouton de bascule Aller/Retour, timeline verticale des arrêts avec pastilles colorées, correspondances avec d'autres lignes, et boutons rapides 'Définir comme départ / arrivée'. |
| `ItineraryFinderView` | `ItineraryFinderView.kt` | Écran de calcul de trajet. Intègre deux sélecteurs déroulants accessibles (`ExposedDropdownMenuBox`), bouton d'inversion des arrêts, suggestions de trajets fréquents (Campus, Marché, Hôpital) et liste des résultats calculés. |
| `ItineraryCard` | `ItineraryFinderView.kt` | Carte de présentation d'une solution d'itinéraire, avec badge distinctif Direct vs Correspondance, calcul du temps total et décomposition détaillée des étapes. |
| `NetworkMapView` | `NetworkMapView.kt` | Plan schématique du réseau dessiné sur un composable `Canvas`. Trace les corridors avec la fonction `drawCorridor`. Détecte les touchers de l'utilisateur avec `pointerInput` et `detectTapGestures`, calcule la distance euclidienne pour sélectionner l'arrêt le plus proche et affiche sa fiche d'information. |
| `FavoritesNotesView` | `FavoritesNotesView.kt` | Écran divisé en deux sections : lignes favorites enregistrées et carnet de mémos de voyage. Intègre le bouton flottant (`FloatingActionButton`) pour ajouter un mémo et les options de suppression. |
| `GuideAndFareView` | `GuideAndFareView.kt` | Écran d'accompagnement de l'usager : simulateur interactif de budget de transport mensuel à deux curseurs (`Slider`), conseils pratiques pour rejoindre le campus universitaire d'Andrainjato, et lexique bilingue Malagasy/Français des expressions en Taxi-be. |

---

## 3. MODÈLES DE DONNÉES & ENTITÉS

- **`BusLine`** : Identifiant (`id`), libellé (`number`), terminus (`departure`, `terminus`), liste ordonnée des arrêts (`stops`), couleur d'identification (`color`), tarif (`fareAriary` : 600 Ar standard, 500 Ar pour L38 et Collectif Barrière CB), note tarifaire (`fareNote`), fréquence (`frequencyMinutes`), horaires (`operatingHours`).
- **`BusStop`** : Nom (`name`), zone urbaine (`zone`), repères visuels (`description`).
- **`ItineraryPlan` & `ItineraryStep`** : Résultats de routage détaillant si le trajet est direct ou avec correspondance, le point de changement (`transferStop`), la durée totale estimée et le tarif cumulé.
- **`FavoriteItem`** : Entité Room (`@Entity(tableName = "favorites")`) sauvegardant les lignes et arrêts favoris de l'usager avec timestamp.
- **`TripNote`** : Entité Room (`@Entity(tableName = "trip_notes")`) sauvegardant les mémos personnels (cours, démarches, marchés) avec référence de transport.

---

## 4. ALGORITHMES DE ROUTAGE DÉTAILLÉS

L'algorithme implémenté dans `FianarBusData.calculateItinerary` fonctionne sans connexion internet :
1. **Passage 1 (Recherche directe)** :
   - Parcourt les lignes du réseau.
   - Pour chaque ligne contenant à la fois le départ et l'arrivée, détermine l'indice de départ (`depIndex`) et l'indice d'arrivée (`arrIndex`).
   - Calcule le nombre d'arrêts (`|arrIndex - depIndex|`), estime la durée (3 minutes par arrêt intermédiaire) et applique le tarif exact de la ligne empruntée.
2. **Passage 2 (Recherche avec correspondance)** :
   - Si un trajet direct n'existe pas ou pour offrir des alternatives, l'algorithme consulte les carrefours pivots majeurs de Fianarantsoa : **Tsianolondroa**, **Ampasambazaha**, **Antarandolo**, **Beravina**.
   - Recherche les lignes menant du départ vers le pivot, puis les lignes menant du pivot vers l'arrivée.
   - Assemble les deux segments en un plan à 1 correspondance avec temps de correspondance estimé (8 minutes de battement) et tarif cumulé des deux lignes.

---

## 5. PLAN DU RÉSEAU SUR CANVAS VECTORIEL

Le composant `NetworkMapView.kt` implémente un rendu cartographique vectoriel performant :
- **Dessin géométrique** : Utilise le `DrawScope` de Compose pour tracer les lignes de couleur reliant les stations sous forme de corridors (`drawCorridor`).
- **Hubs centraux** : Dessine les pôles d'échange majeurs avec un cercle extérieur contrasté et un point central coloré.
- **Interactivité tactile** : `Modifier.pointerInput` intercepte les clics sur l'écran. Une fonction de calcul de distance euclidienne $\sqrt{(x_2-x_1)^2 + (y_2-y_1)^2}$ identifie la station la plus proche du point touché (avec un rayon de tolérance de 35dp) et met à jour la station sélectionnée pour afficher immédiatement sa fiche détaillée.

---

## 6. THÈME, ACCESSIBILITÉ & TESTS

- **Thème Material 3 (`Color.kt`, `Theme.kt`)** : Palette de couleurs soigneusement choisie (Vert forêt émeraude `#004D40`, Terre ocre `#E65100`, Brique latérite `#D32F2F`) reflétant l'identité de Fianarantsoa et assurant un ratio de contraste supérieur aux normes WCAG AA.
- **Accessibilité** : Toutes les zones cliquables mesurent au moins 48dp x 48dp (`minimumInteractiveComponentSize`) et chaque icône dispose d'un `contentDescription` explicite pour les lecteurs d'écran (TalkBack).
- **TestTags** : Chaque élément interactif (`navigation_tab_*`, `search_input`, `swap_stops_button`, etc.) est doté d'un `Modifier.testTag` pour permettre les tests d'automatisation.
- **Tests** :
  - `ExampleRobolectricTest.kt` : Exécute des tests locaux sur la JVM pour vérifier le chargement des ressources Android et des données de transport sans émulateur physique.
  - `GreetingScreenshotTest.kt` : Test visuel Roborazzi validant la conformité graphique de l'en-tête de l'application.
