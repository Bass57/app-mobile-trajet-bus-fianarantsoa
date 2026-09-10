package com.example.data.datasource

import androidx.compose.ui.graphics.Color
import com.example.data.model.BusLine
import com.example.data.model.BusStop
import com.example.data.model.NetworkNode
import com.example.data.model.TransitVocabulary

object FianarBusData {

    val STOPS = listOf(
        BusStop("tsianolondroa", "Tsianolondroa", "Centre-Ville", isHub = true, "Grand carrefour central, Marché du Zoma, banques et commerces"),
        BusStop("ampasambazaha", "Ampasambazaha", "Centre-Ville", isHub = true, "Poste centrale, pharmacies, boutiques et liaisons administratives"),
        BusStop("andrainjato", "Andrainjato Université", "Campus & Éducation", isHub = true, "Campus universitaire de Fianarantsoa, ENI, Faculté des Sciences"),
        BusStop("kianjasoa", "Kianjasoa", "Zone Universitaire", isHub = false, "Zone étudiante, proximité complexe sportif et résidences"),
        BusStop("antarandolo", "Antarandolo", "Zone Est", isHub = true, "Carrefour clé reliant le centre et la route d'Andrainjato"),
        BusStop("tanambao", "Tanambao", "Quartier Populaire", isHub = false, "Grand quartier d'habitation et commerces de proximité"),
        BusStop("gare_fce", "Gare FCE", "Gare & Historique", isHub = true, "Gare du train mythique Fianarantsoa-Côte Est vers Manakara"),
        BusStop("ankazobe", "Ankazobe", "Zone Nord", isHub = false, "Quartier nord, zone résidentielle et maraîchère"),
        BusStop("ambalapaiso", "Ambalapaiso", "Zone Nord-Ouest", isHub = false, "Quartier artisanal et résidentiel"),
        BusStop("ambalambositra", "Ambalambositra", "Zone Nord", isHub = false, "Quartier d'habitation nord et artisanat"),
        BusStop("anjoma", "Anjoma", "Zone Commerciale", isHub = false, "Grand marché couvert et commerces d'Anjoma"),
        BusStop("mahazengy", "Mahazengy", "Zone Sud", isHub = false, "Quartier résidentiel sud et accès collège"),
        BusStop("tambohobe", "Tambohobe (CHU)", "Santé", isHub = true, "Centre Hospitalier Universitaire de Tambohobe"),
        BusStop("hopitaly_be", "Hopitaly Be", "Santé", isHub = false, "Hôpital central et dispensaire"),
        BusStop("sahalava", "Sahalava", "Zone Ouest", isHub = false, "Quartier résidentiel sur les collines ouest"),
        BusStop("beravina", "Beravina", "Zone Sud", isHub = false, "Zone d'habitation et ateliers artisanaux"),
        BusStop("mahamanina", "Mahamanina", "Zone Sud-Est", isHub = false, "Quartier résidentiel calme et école"),
        BusStop("ankofafa", "Ankofafa", "Zone Est", isHub = false, "Quartier agricole et marché périphérique"),
        BusStop("ambatomena", "Ambatomena", "Zone Centre-Sud", isHub = false, "Quartier intermédiaire avec églises et écoles"),
        BusStop("haute_ville", "Haute-Ville (Rova)", "Historique & Tourisme", isHub = true, "Vieille ville, Cathédrale Ambozontany, vue panoramique"),
        BusStop("ambozontany", "Ambozontany", "Historique", isHub = false, "Montée vers la cathédrale, escaliers historiques"),
        BusStop("talatamaty", "Talatamaty", "Zone Sud-Ouest", isHub = false, "Quartier commerçant et marché du mardi"),
        BusStop("isaha", "Isaha", "Zone Sud-Ouest", isHub = false, "Terminus ouest et quartier résidentiel"),
        BusStop("morafeno", "Morafeno", "Zone Périphérique", isHub = false, "Quartier populaire et artisans du bois"),
        BusStop("barriere_andrainjato", "Barrière d'Andrainjato", "Campus & Université", isHub = true, "Barrage de contrôle et point d'arrêt à l'entrée du campus, tête de ligne des bus collectifs"),
        BusStop("faculte_sciences", "Faculté des Sciences", "Campus & Université", isHub = false, "Bâtiments académiques et laboratoires de recherche"),
        BusStop("ambatovory", "Ambatovory", "Zone Ouest", isHub = true, "Quartier Ambatovory, Aumônerie Gendarmerie et Fokontany Tsaramandroso"),
        BusStop("soanierana", "Soanierana", "Zone Sud", isHub = true, "Quartier Soanierana, Tobim-pifaliana FLM et Institut Supérieur"),
        BusStop("tsaramandroso", "Tsaramandroso", "Zone Centre-Ouest", isHub = false, "Quartier Tsaramandroso et artisans"),
        BusStop("antanimena", "Antanimena", "Zone Est", isHub = false, "Terrain de football Antanimena et quartier Ankofafa"),
        BusStop("ankofafa_andrefana", "Ankofafa Andrefana", "Zone Est", isHub = false, "Quartier résidentiel ouest d'Ankofafa")
    )

    val LINES = listOf(
        BusLine(
            id = "L38",
            number = "Ligne 38",
            name = "Tsianolondroa ⇄ Andrainjato (Campus)",
            departure = "Tsianolondroa",
            terminus = "Andrainjato Université",
            color = Color(0xFFD32F2F), // Rouge vif
            stops = listOf(
                "Tsianolondroa",
                "Ampasambazaha",
                "Tanambao",
                "Antarandolo",
                "Kianjasoa",
                "Andrainjato Université"
            ),
            landmarks = listOf("Marché Zoma", "Poste Ampasambazaha", "Campus Andrainjato", "ENI"),
            frequencyMinutes = "3 - 5 min",
            operatingHours = "05:30 - 19:30",
            fareAriary = 500,
            fareNote = "Tarif étudiant dérogatoire (500 Ar)",
            description = "Ligne universitaire emblématique de Fianarantsoa reliant directement le centre au campus d'Andrainjato. Bénéficie de l'exception tarifaire étudiante à 500 Ar (au lieu de 600 Ar).",
            isPopularForStudents = true
        ),
        BusLine(
            id = "CB",
            number = "Collectif Barrière (CB)",
            name = "Barrière d'Andrainjato ⇄ Campus Universitaire",
            departure = "Barrière d'Andrainjato",
            terminus = "Andrainjato Université",
            color = Color(0xFF00897B), // Sarcelle / Vert émeraude
            stops = listOf(
                "Barrière d'Andrainjato",
                "Kianjasoa",
                "Faculté des Sciences",
                "Andrainjato Université"
            ),
            landmarks = listOf("Barrage d'Andrainjato", "Kianjasoa", "Faculté des Sciences", "Campus ENI / Rectorat"),
            frequencyMinutes = "En continu (dès remplissage)",
            operatingHours = "06:00 - 18:30",
            fareAriary = 500,
            fareNote = "Tarif étudiant dérogatoire (500 Ar)",
            description = "Bus collectif et navette de proximité assurant la liaison entre le barrage/barrière et le campus universitaire d'Andrainjato. Tarif étudiant de 500 Ar.",
            isPopularForStudents = true
        ),
        BusLine(
            id = "L40",
            number = "Ligne 40",
            name = "Ankofafa ⇄ Andrainjato (via Zoma)",
            departure = "Ankofafa",
            terminus = "Andrainjato Université",
            color = Color(0xFFE65100), // Orange vif
            stops = listOf(
                "Ankofafa",
                "Morafeno",
                "Tsianolondroa",
                "Ampasambazaha",
                "Antarandolo",
                "Kianjasoa",
                "Andrainjato Université"
            ),
            landmarks = listOf("Marché Ankofafa", "Marché Zoma", "Poste Ampasambazaha", "Campus"),
            frequencyMinutes = "5 - 7 min",
            operatingHours = "05:30 - 19:00",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Ligne majeure reliant la zone est d'Ankofafa et le centre de Tsianolondroa à l'Université.",
            isPopularForStudents = true
        ),
        BusLine(
            id = "L30",
            number = "Ligne 30",
            name = "Tsianolondroa ⇄ Ankofafa (via Gare FCE)",
            departure = "Tsianolondroa",
            terminus = "Ankofafa",
            color = Color(0xFF2E7D32), // Vert
            stops = listOf(
                "Tsianolondroa",
                "Ampasambazaha",
                "Gare FCE",
                "Morafeno",
                "Ankofafa"
            ),
            landmarks = listOf("Marché Zoma", "Gare FCE (Ligne train Manakara)", "Marché Ankofafa"),
            frequencyMinutes = "6 - 8 min",
            operatingHours = "06:00 - 18:30",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Desserte essentielle reliant le centre-ville à la gare ferroviaire FCE et au marché périphérique d'Ankofafa."
        ),
        BusLine(
            id = "L48",
            number = "Ligne 48",
            name = "Ambalambositra ⇄ Cité des Profs (Andrainjato)",
            departure = "Ambalambositra",
            terminus = "Andrainjato Université",
            color = Color(0xFF7B1FA2), // Violet
            stops = listOf(
                "Ambalambositra",
                "Ambalapaiso",
                "Tsianolondroa",
                "Antarandolo",
                "Kianjasoa",
                "Andrainjato Université"
            ),
            landmarks = listOf("Ambalambositra", "Marché Zoma", "Cité des Professeurs", "Faculté des Sciences"),
            frequencyMinutes = "8 - 10 min",
            operatingHours = "05:45 - 19:15",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Ligne estudiantine et professorale reliant les quartiers nord directement au campus.",
            isPopularForStudents = true
        ),
        BusLine(
            id = "L21",
            number = "Ligne 21",
            name = "Mahazengy ⇄ Tambohobe (CHU)",
            departure = "Mahazengy",
            terminus = "Tambohobe (CHU)",
            color = Color(0xFF1976D2), // Bleu
            stops = listOf(
                "Mahazengy",
                "Ambatomena",
                "Ampasambazaha",
                "Hopitaly Be",
                "Tambohobe (CHU)"
            ),
            landmarks = listOf("Mahazengy", "Poste Ampasambazaha", "Hopitaly Be", "CHU Tambohobe"),
            frequencyMinutes = "7 - 10 min",
            operatingHours = "05:45 - 18:45",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Ligne hospitalière clé desservant le Centre Hospitalier Universitaire de Tambohobe et Hopitaly Be."
        ),
        BusLine(
            id = "L22",
            number = "Ligne 22",
            name = "Haute-Ville (Rova) ⇄ Ampasambazaha",
            departure = "Haute-Ville (Rova)",
            terminus = "Ampasambazaha",
            color = Color(0xFFC2185B), // Framboise
            stops = listOf(
                "Haute-Ville (Rova)",
                "Ambozontany",
                "Tsianolondroa",
                "Ampasambazaha"
            ),
            landmarks = listOf("Cathédrale Ambozontany", "Rova historique", "Poste Ampasambazaha"),
            frequencyMinutes = "10 - 12 min",
            operatingHours = "06:00 - 18:00",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Ligne historique grimpant les collines vers la vieille ville, la cathédrale et le belvédère du Rova."
        ),
        BusLine(
            id = "L26",
            number = "Ligne 26",
            name = "Anjoma ⇄ Beravina (via Tsianolondroa)",
            departure = "Anjoma",
            terminus = "Beravina",
            color = Color(0xFF00838F), // Sarcelle / Cyan
            stops = listOf(
                "Anjoma",
                "Tsianolondroa",
                "Ampasambazaha",
                "Ambatomena",
                "Beravina"
            ),
            landmarks = listOf("Marché Anjoma", "Centre Tsianolondroa", "Ateliers Beravina"),
            frequencyMinutes = "8 - 10 min",
            operatingHours = "06:00 - 18:30",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Ligne commerçante reliant le grand marché d'Anjoma au sud vers Beravina."
        ),
        BusLine(
            id = "L28",
            number = "Ligne 28",
            name = "Beravina ⇄ Mahamanina",
            departure = "Beravina",
            terminus = "Mahamanina",
            color = Color(0xFFBF360C), // Terracotta
            stops = listOf(
                "Beravina",
                "Ambatomena",
                "Ampasambazaha",
                "Tsianolondroa",
                "Mahamanina"
            ),
            landmarks = listOf("Beravina", "Poste Ampasambazaha", "Mahamanina"),
            frequencyMinutes = "8 - 12 min",
            operatingHours = "06:00 - 18:30",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Desserte des quartiers sud et des zones d'habitation de Mahamanina."
        ),
        BusLine(
            id = "L34",
            number = "Ligne 34",
            name = "Ankazobe ⇄ Sahalava",
            departure = "Ankazobe",
            terminus = "Sahalava",
            color = Color(0xFF00695C), // Vert émeraude
            stops = listOf(
                "Ankazobe",
                "Ambalapaiso",
                "Tsianolondroa",
                "Ampasambazaha",
                "Tambohobe (CHU)",
                "Sahalava"
            ),
            landmarks = listOf("Ankazobe", "Marché Zoma", "CHU Tambohobe", "Colline Sahalava"),
            frequencyMinutes = "7 - 10 min",
            operatingHours = "05:45 - 19:00",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Traversée nord-ouest reliant Ankazobe à la colline résidentielle de Sahalava."
        ),
        BusLine(
            id = "L39",
            number = "Ligne 39",
            name = "Talatamaty ⇄ Tsianolondroa (via Isaha)",
            departure = "Talatamaty",
            terminus = "Tsianolondroa",
            color = Color(0xFF4E342E), // Brun chaud
            stops = listOf(
                "Talatamaty",
                "Isaha",
                "Ampasambazaha",
                "Gare FCE",
                "Tsianolondroa"
            ),
            landmarks = listOf("Marché Talatamaty", "Quartier Isaha", "Gare FCE", "Zoma"),
            frequencyMinutes = "10 min",
            operatingHours = "06:00 - 18:00",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Navette des quartiers ouest (Talatamaty, Isaha) vers la gare ferroviaire et le marché du Zoma."
        ),
        BusLine(
            id = "L32",
            number = "Ligne 32",
            name = "Ambatovory ⇄ Soanierana",
            departure = "Ambatovory",
            terminus = "Soanierana",
            color = Color(0xFF00ACC1), // Cyan / Bleu canard
            stops = listOf(
                "Ambatovory",
                "Tsaramandroso",
                "Ampasambazaha",
                "Tsianolondroa",
                "Soanierana"
            ),
            landmarks = listOf("Aumônerie Gendarmerie", "Fokontany Tsaramandroso", "Poste Ampasambazaha", "FLM Tobim-pifaliana Soanierana", "Institut Supérieur"),
            frequencyMinutes = "8 - 10 min",
            operatingHours = "06:00 - 18:30",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Liaison transversale reliant le quartier ouest d'Ambatovory et Tsaramandroso au sud vers Soanierana via le centre-ville."
        ),
        BusLine(
            id = "L33",
            number = "Ligne 33",
            name = "Tsianolondroa ⇄ Ankofafa",
            departure = "Tsianolondroa",
            terminus = "Ankofafa",
            color = Color(0xFF689F38), // Vert olive vif
            stops = listOf(
                "Tsianolondroa",
                "Morafeno",
                "Antanimena",
                "Ankofafa"
            ),
            landmarks = listOf("Marché Zoma", "Morafeno", "Terrain Antanimena", "Poste de police Ankofafa"),
            frequencyMinutes = "6 - 8 min",
            operatingHours = "06:00 - 18:30",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Ligne directe très fréquentée reliant le carrefour central de Tsianolondroa au quartier est d'Ankofafa via Antanimena."
        ),
        BusLine(
            id = "L29",
            number = "Ligne 29",
            name = "Ambozontany ⇄ Ankofafa Andrefana",
            departure = "Ambozontany",
            terminus = "Ankofafa Andrefana",
            color = Color(0xFF5C6BC0), // Indigo
            stops = listOf(
                "Ambozontany",
                "Tsianolondroa",
                "Morafeno",
                "Ankofafa Andrefana"
            ),
            landmarks = listOf("Cathédrale Ambozontany", "Marché Zoma", "Morafeno", "Ankofafa Ouest"),
            frequencyMinutes = "10 - 12 min",
            operatingHours = "06:00 - 18:00",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Liaison reliant les hauteurs d'Ambozontany à la partie ouest d'Ankofafa."
        ),
        BusLine(
            id = "L23",
            number = "Ligne 23",
            name = "Anjoma ⇄ Ankofafa",
            departure = "Anjoma",
            terminus = "Ankofafa",
            color = Color(0xFFFF8F00), // Ambre foncé
            stops = listOf(
                "Anjoma",
                "Tsianolondroa",
                "Ampasambazaha",
                "Morafeno",
                "Ankofafa"
            ),
            landmarks = listOf("Grand Marché d'Anjoma", "Centre Tsianolondroa", "Gare / Morafeno", "Ankofafa"),
            frequencyMinutes = "8 - 10 min",
            operatingHours = "06:00 - 18:30",
            fareAriary = 600,
            fareNote = "Tarif standard (600 Ar)",
            description = "Ligne commerçante reliant le marché couvert d'Anjoma à Ankofafa."
        )
    )

    val VOCABULARY = listOf(
        TransitVocabulary(
            malagasy = "Misy miala e !",
            phonetic = "[Mi-chi mi-al-é]",
            french = "Arrêt s'il vous plaît ! (Quelqu'un descend)",
            context = "À crier fermement et poliment au chauffeur ou receveur 50 mètres avant votre arrêt."
        ),
        TransitVocabulary(
            malagasy = "Frais azafady",
            phonetic = "[Fré a-za-fa-di]",
            french = "Voici le tarif s'il vous plaît",
            context = "En tendant votre monnaie au receveur : 600 Ar tarif standard (ou 500 Ar sur la ligne 38 et le collectif barrière CB)."
        ),
        TransitVocabulary(
            malagasy = "Mbola misy toerana ve ?",
            phonetic = "[Mbou-la mi-chi tou-é-ra-na vé]",
            french = "Y a-t-il encore de la place ?",
            context = "Avant de monter dans un bus apparemment bondé."
        ),
        TransitVocabulary(
            malagasy = "Andrainjato ve mandeha ?",
            phonetic = "[An-drain-dza-tou vé man-dé-ha]",
            french = "Allez-vous à Andrainjato ?",
            context = "Pour confirmer la destination avant de monter."
        ),
        TransitVocabulary(
            malagasy = "Apetraho eto azafady",
            phonetic = "[A-pé-tra-hou é-tou a-za-fa-di]",
            french = "Déposez-moi ici s'il vous plaît",
            context = "Si vous arrivez juste à l'angle ou au repère souhaité."
        ),
        TransitVocabulary(
            malagasy = "Misaotra tompoko",
            phonetic = "[Mi-saoutr' toumpk]",
            french = "Merci beaucoup",
            context = "En descendant du taxi-be."
        )
    )

    // Schematic nodes positioned proportionally for the network map
    val NETWORK_NODES = listOf(
        NetworkNode("Ankazobe", 0.35f, 0.10f, false, listOf("L34")),
        NetworkNode("Ambalapaiso", 0.25f, 0.22f, false, listOf("L34", "L48")),
        NetworkNode("Haute-Ville (Rova)", 0.60f, 0.15f, true, listOf("L22")),
        NetworkNode("Ambozontany", 0.55f, 0.28f, false, listOf("L22", "L29")),
        NetworkNode("Ambatovory", 0.12f, 0.30f, true, listOf("L32")),
        NetworkNode("Tsaramandroso", 0.22f, 0.34f, false, listOf("L32")),
        NetworkNode("Tsianolondroa", 0.45f, 0.38f, true, listOf("L38", "L40", "L30", "L48", "L22", "L26", "L28", "L34", "L39", "L32", "L33", "L29", "L23")),
        NetworkNode("Ampasambazaha", 0.35f, 0.48f, true, listOf("L38", "L40", "L30", "L21", "L22", "L26", "L28", "L34", "L39", "L32", "L23")),
        NetworkNode("Gare FCE", 0.58f, 0.48f, true, listOf("L30", "L39")),
        NetworkNode("Tanambao", 0.45f, 0.58f, false, listOf("L38")),
        NetworkNode("Antarandolo", 0.62f, 0.60f, true, listOf("L38", "L40", "L48")),
        NetworkNode("Kianjasoa", 0.75f, 0.68f, false, listOf("L38", "L40", "L48", "CB")),
        NetworkNode("Barrière d'Andrainjato", 0.80f, 0.74f, true, listOf("CB")),
        NetworkNode("Andrainjato Université", 0.88f, 0.78f, true, listOf("L38", "L40", "L48", "CB")),
        NetworkNode("Tambohobe (CHU)", 0.18f, 0.40f, true, listOf("L21", "L34")),
        NetworkNode("Sahalava", 0.10f, 0.52f, false, listOf("L34")),
        NetworkNode("Isaha", 0.15f, 0.70f, false, listOf("L39")),
        NetworkNode("Talatamaty", 0.28f, 0.78f, false, listOf("L39")),
        NetworkNode("Ambatomena", 0.38f, 0.72f, false, listOf("L21", "L26", "L28")),
        NetworkNode("Beravina", 0.48f, 0.82f, false, listOf("L26", "L28")),
        NetworkNode("Mahamanina", 0.60f, 0.90f, false, listOf("L28")),
        NetworkNode("Soanierana", 0.42f, 0.92f, true, listOf("L32")),
        NetworkNode("Ankofafa Andrefana", 0.74f, 0.40f, false, listOf("L29")),
        NetworkNode("Antanimena", 0.73f, 0.48f, false, listOf("L33")),
        NetworkNode("Ankofafa", 0.82f, 0.48f, false, listOf("L40", "L30", "L33", "L23"))
    )

    fun findStopByName(name: String): BusStop? {
        return STOPS.find { it.name.equals(name, ignoreCase = true) }
    }

    fun findLinesPassingByStop(stopName: String): List<BusLine> {
        return LINES.filter { line ->
            line.stops.any { it.equals(stopName, ignoreCase = true) }
        }
    }
}
