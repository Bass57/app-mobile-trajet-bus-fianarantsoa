# 🚌 Bus Fianarantsoa (Madagascar)

Application mobile Android native d'aide à la mobilité urbaine et au transport en commun pour la ville de **Fianarantsoa**, Madagascar.

Développée avec **Kotlin** et **Jetpack Compose (Material Design 3)**, l'application fonctionne **100% hors-ligne** grâce à la base de données locale **Room (SQLite)** et à son moteur de calcul d'itinéraire embarqué.

---

## 📱 Aperçu des Fonctionnalités

- **📋 Annuaire complet des 10 Lignes Urbaines (L1 à L10) :**
  - Tracé ordonné de tous les arrêts desservis (Aller & Retour).
  - Terminus, fréquence de passage (ex: 5-7 min), horaires d'exploitation (05h30 - 19h30) et tarif unitaire (500 Ar).
  - Filtres thématiques par centres d'intérêt : *Université / Campus*, *Marchés*, *Hôpitaux / Santé*, *Gare FCE*.
  - Recherche instantanée par nom de ligne, terminus ou nom d'arrêt.

- **🧭 Calculateur d'Itinéraire Intelligent :**
  - **Trajets Directs :** identification des lignes sans correspondance, calcul du nombre d'arrêts, temps de parcours estimé (3 min/arrêt) et tarif fixe (500 Ar).
  - **Trajets avec Correspondance :** exploration des carrefours d'échange stratégiques (**Tsianolondroa**, **Ampasambazaha**, **Antarandolo**, **Beravina**) pour assembler un trajet en deux étapes avec estimation du temps de correspondance (8 min) et tarif cumulé (1000 Ar).
  - Bouton d'inversion rapide Départ ⇄ Arrivée et raccourcis vers les trajets fréquents.

- **🗺️ Plan Schématique Interactif du Réseau :**
  - Rendu cartographique vectoriel haute performance dessiné sur un composable `Canvas`.
  - Corridors de transport colorés selon les teintes officielles de chaque ligne.
  - Interaction tactile (`pointerInput`) : toucher une station pour afficher instantanément sa fiche descriptive et ses correspondances.

- **⭐ Favoris & Carnet de Mémos Hors-Ligne :**
  - Sauvegarde locale des lignes et arrêts fréquents en un seul clic.
  - Carnet de notes personnelles pour mémoriser les horaires de cours, les démarches ou les jours de marché.
  - Persistance assurée par la base de données SQLite embarquée (Room).

- **🎓 Guide Usager, Simulateur & Lexique :**
  - **Simulateur de budget de transport :** curseurs interactifs (*trajets par jour* et *jours de déplacement par mois*) pour estimer ses dépenses mensuelles en Ariary.
  - **Guide étudiant :** repères et conseils pratiques pour rejoindre facilement le campus d'Andrainjato.
  - **Lexique Taxi-be (Teny Fampiasa) :** guide bilingue Malagasy / Français avec phonétique et contexte culturel (*"Misy miala e !"*, *"Mbola misy ve ?"*...).

---

## 🏗️ Architecture & Stack Technique

L'application respecte les principes de la **Clean Architecture** et du patron **MVVM (Model - View - ViewModel)** avec un **Flux Unidirectionnel des Données (UDF)**.

- **Langage :** Kotlin
- **Interface Utilisateur :** Jetpack Compose, Material Design 3 (M3)
- **Persistance Locale :** Room Database (SQLite) avec coroutines & `Flow` réactifs
- **Gestion de l'État :** `ViewModel`, `MutableStateFlow` et `collectAsStateWithLifecycle()`
- **Affichage Écran :** Edge-to-Edge (`enableEdgeToEdge()`) avec gestion stricte des `WindowInsets`
- **Accessibilité :** Cibles tactiles $\ge$ 48dp x 48dp, contrastes conformes aux critères WCAG et balises sémantiques TalkBack
- **Tests :** JUnit 4, Robolectric (tests locaux sans émulateur) et Roborazzi (tests de régression visuelle)

---

## 📂 Structure du Code Source

```text
app/src/main/java/com/example/
├── data/
│   ├── datasource/
│   │   └── FianarBusData.kt       # Catalogue des 10 lignes, arrêts et algorithmes d'itinéraire
│   ├── local/
│   │   ├── BusEntities.kt         # Entités Room (FavoriteItem, TripNote)
│   │   ├── BusDao.kt              # Requêtes SQL réactives (Flow, suspend)
│   │   └── BusDatabase.kt         # Singleton thread-safe de la base Room SQLite
│   ├── model/
│   │   └── BusModels.kt           # Modèles métiers (BusLine, BusStop, ItineraryPlan...)
│   └── repository/
│       └── BusRepository.kt       # Source unique de vérité et opérations CRUD
├── ui/
│   ├── components/
│   │   ├── BusLineCard.kt         # Cartes de lignes et vue annuaire
│   │   ├── BusLineDetailModal.kt  # ModalBottomSheet avec timeline verticale des arrêts
│   │   ├── ItineraryFinderView.kt # Sélecteurs et moteur visuel d'itinéraire
│   │   ├── NetworkMapView.kt      # Plan schématique vectoriel Canvas tactile
│   │   ├── FavoritesNotesView.kt  # Gestion des favoris et carnet de mémos
│   │   └── GuideAndFareView.kt    # Simulateur de budget et lexique bilingue
│   ├── theme/
│   │   ├── Color.kt               # Palette de couleurs inspirée de Madagascar
│   │   ├── Theme.kt               # Thème Material Design 3 (sombre & clair)
│   │   └── Type.kt                # Typographie Material 3
│   └── viewmodel/
│       └── BusViewModel.kt        # État BusUiState et logique de présentation
└── MainActivity.kt                # Point d'entrée de l'application et navigation M3
```

---

## 🚀 Compilation & Exécution

### Prérequis
- Android SDK (API 34 / Android 14)
- JDK 17 ou supérieur
- Gradle (Kotlin DSL)

### Commandes utiles

```bash
# Compiler le projet en mode Debug
gradle :app:assembleDebug

# Exécuter les tests unitaires et tests Robolectric
gradle :app:testDebugUnitTest

# Vérifier les tests de régression visuelle (Roborazzi)
gradle :app:verifyRoborazziDebug
```

---

## 📖 Documentation Détaillée

Pour une explication exhaustive de chaque fonction, classe, algorithme et composant du projet, consultez les documents inclus à la racine du dépôt :
- **`GUIDE_CODE_EXPLICATION.pdf`** : Guide complet formaté de 7 pages avec tables des signatures et rôles.
- **`GUIDE_CODE_EXPLICATION.md`** : Guide technique complet au format Markdown.

---

## 👤 Auteur & Crédits

- **Auteur :** Bastien Manana
- **Projet :** Transport Urbain de Fianarantsoa • Madagascar
- **Licence :** Projet open-source sous licence libre d'utilisation.
