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
        BusStop("morafeno", "Morafeno", "Zone Périphérique", isHub = false, "Quartier populaire et artisans du bois")
    )

    val LINES = listOf(
        BusLine(
            id = "L1",
            number = "Ligne 1",
            name = "Ankazobe ⇄ Andrainjato (Campus)",
            departure = "Ankazobe",
            terminus = "Andrainjato Université",
            color = Color(0xFFD32F2F), // Rouge
            stops = listOf(
                "Ankazobe",
                "Ambalapaiso",
                "Tsianolondroa",
                "Ampasambazaha",
                "Tanambao",
                "Antarandolo",
                "Kianjasoa",
                "Andrainjato Université"
            ),
            landmarks = listOf("Marché Zoma", "Poste Ampasambazaha", "Campus Universitaire", "ENI"),
            frequencyMinutes = "5 - 7 min",
            operatingHours = "05:30 - 19:15",
            fareAriary = 500,
            description = "Ligne principale très fréquentée par les étudiants de l'Université de Fianarantsoa et les enseignants.",
            isPopularForStudents = true
        ),
        BusLine(
            id = "L2",
            number = "Ligne 2",
            name = "Sahalava ⇄ Mahamanina",
            departure = "Sahalava",
            terminus = "Mahamanina",
            color = Color(0xFF1976D2), // Bleu
            stops = listOf(
                "Sahalava",
                "Tambohobe (CHU)",
                "Hopitaly Be",
                "Tsianolondroa",
                "Ampasambazaha",
                "Ambatomena",
                "Beravina",
                "Mahamanina"
            ),
            landmarks = listOf("CHU Tambohobe", "Pharmacie Centrale", "Quartier Beravina"),
            frequencyMinutes = "7 - 10 min",
            operatingHours = "05:45 - 18:45",
            fareAriary = 500,
            description = "Traverse la ville d'ouest en sud-est, idéale pour rejoindre le CHU Tambohobe et le centre."
        ),
        BusLine(
            id = "L3",
            number = "Ligne 3",
            name = "Kianjasoa ⇄ Ankofafa (via Gare FCE)",
            departure = "Kianjasoa",
            terminus = "Ankofafa",
            color = Color(0xFF2E7D32), // Vert
            stops = listOf(
                "Kianjasoa",
                "Antarandolo",
                "Gare FCE",
                "Ampasambazaha",
                "Tsianolondroa",
                "Morafeno",
                "Ankofafa"
            ),
            landmarks = listOf("Gare FCE (Train Manakara)", "Poste Ampasambazaha", "Marché Ankofafa"),
            frequencyMinutes = "8 - 10 min",
            operatingHours = "06:00 - 18:30",
            fareAriary = 500,
            description = "Liaison directe avec la gare ferroviaire FCE et les quartiers est."
        ),
        BusLine(
            id = "L4",
            number = "Ligne 4",
            name = "Haute-Ville (Rova) ⇄ Talatamaty",
            departure = "Haute-Ville (Rova)",
            terminus = "Talatamaty",
            color = Color(0xFFE65100), // Orange
            stops = listOf(
                "Haute-Ville (Rova)",
                "Ambozontany",
                "Tsianolondroa",
                "Ampasambazaha",
                "Isaha",
                "Talatamaty"
            ),
            landmarks = listOf("Cathédrale Ambozontany", "Vue panoramique Rova", "Marché Talatamaty"),
            frequencyMinutes = "10 - 12 min",
            operatingHours = "06:00 - 18:00",
            fareAriary = 500,
            description = "Permet de grimper vers la Haute-Ville historique et relie le marché de Talatamaty."
        ),
        BusLine(
            id = "L5",
            number = "Ligne 5",
            name = "Ambalapaiso ⇄ Andrainjato (via CHU)",
            departure = "Ambalapaiso",
            terminus = "Andrainjato Université",
            color = Color(0xFF7B1FA2), // Violet
            stops = listOf(
                "Ambalapaiso",
                "Tambohobe (CHU)",
                "Hopitaly Be",
                "Tsianolondroa",
                "Antarandolo",
                "Kianjasoa",
                "Andrainjato Université"
            ),
            landmarks = listOf("CHU Tambohobe", "Faculté de Droit & Éco", "Cité Universitaire"),
            frequencyMinutes = "6 - 8 min",
            operatingHours = "05:30 - 19:30",
            fareAriary = 500,
            description = "Seconde ligne universitaire desservant les étudiants logeant près de Tambohobe.",
            isPopularForStudents = true
        ),
        BusLine(
            id = "L6",
            number = "Ligne 6",
            name = "Tanambao ⇄ Beravina",
            departure = "Tanambao",
            terminus = "Beravina",
            color = Color(0xFF00838F), // Sarcelle / Cyan
            stops = listOf(
                "Tanambao",
                "Antarandolo",
                "Gare FCE",
                "Tsianolondroa",
                "Ampasambazaha",
                "Ambatomena",
                "Beravina"
            ),
            landmarks = listOf("Gare FCE", "Commerces Tanambao", "Ambatomena"),
            frequencyMinutes = "8 - 10 min",
            operatingHours = "06:00 - 18:30",
            fareAriary = 500,
            description = "Interconnexion rapide entre les quartiers Tanambao, la Gare et Beravina."
        ),
        BusLine(
            id = "L7",
            number = "Ligne 7",
            name = "Ankofafa ⇄ Sahalava",
            departure = "Ankofafa",
            terminus = "Sahalava",
            color = Color(0xFFAD1457), // Framboise / Rose
            stops = listOf(
                "Ankofafa",
                "Ambatomena",
                "Ampasambazaha",
                "Hopitaly Be",
                "Tambohobe (CHU)",
                "Sahalava"
            ),
            landmarks = listOf("Marché Ankofafa", "CHU Tambohobe", "Centre médical"),
            frequencyMinutes = "10 min",
            operatingHours = "06:00 - 18:15",
            fareAriary = 500,
            description = "Relie l'est agricole aux hauteurs de Sahalava sans passer par le grand marché central."
        ),
        BusLine(
            id = "L8",
            number = "Ligne 8",
            name = "Mahamanina ⇄ Andrainjato (Direct Sud)",
            departure = "Mahamanina",
            terminus = "Andrainjato Université",
            color = Color(0xFFBF360C), // Terracotta foncé
            stops = listOf(
                "Mahamanina",
                "Beravina",
                "Ambatomena",
                "Tsianolondroa",
                "Antarandolo",
                "Kianjasoa",
                "Andrainjato Université"
            ),
            landmarks = listOf("Sud Mahamanina", "Tsianolondroa", "Campus Andrainjato"),
            frequencyMinutes = "8 - 12 min",
            operatingHours = "05:45 - 19:00",
            fareAriary = 500,
            description = "Ligne express reliant les zones sud directement au campus universitaire.",
            isPopularForStudents = true
        ),
        BusLine(
            id = "L9",
            number = "Ligne 9",
            name = "Isaha ⇄ Tsianolondroa (via Gare)",
            departure = "Isaha",
            terminus = "Tsianolondroa",
            color = Color(0xFF4E342E), // Brun chaud
            stops = listOf(
                "Isaha",
                "Talatamaty",
                "Ampasambazaha",
                "Gare FCE",
                "Tsianolondroa"
            ),
            landmarks = listOf("Talatamaty", "Gare ferroviaire", "Marché Zoma"),
            frequencyMinutes = "10 min",
            operatingHours = "06:00 - 18:00",
            fareAriary = 500,
            description = "Navette directe des quartiers ouest vers le grand marché central et la gare."
        ),
        BusLine(
            id = "L10",
            number = "Ligne 10",
            name = "Ankazobe ⇄ Haute-Ville (Ambozontany)",
            departure = "Ankazobe",
            terminus = "Haute-Ville (Rova)",
            color = Color(0xFF00695C), // Vert forêt
            stops = listOf(
                "Ankazobe",
                "Ambalapaiso",
                "Tsianolondroa",
                "Ambozontany",
                "Haute-Ville (Rova)"
            ),
            landmarks = listOf("Vue d'Ankazobe", "Marché Tsianolondroa", "Cathédrale Haute-Ville"),
            frequencyMinutes = "12 - 15 min",
            operatingHours = "06:15 - 18:00",
            fareAriary = 500,
            description = "Ligne touristique et historique montant du nord jusqu'au sommet du Rova."
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
            context = "En tendant votre billet de 500 Ar au receveur (mpandray vola)."
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
        NetworkNode("Ankazobe", 0.35f, 0.10f, false, listOf("L1", "L10")),
        NetworkNode("Ambalapaiso", 0.25f, 0.22f, false, listOf("L1", "L5", "L10")),
        NetworkNode("Haute-Ville (Rova)", 0.60f, 0.15f, true, listOf("L4", "L10")),
        NetworkNode("Ambozontany", 0.55f, 0.28f, false, listOf("L4", "L10")),
        NetworkNode("Tsianolondroa", 0.45f, 0.38f, true, listOf("L1", "L2", "L3", "L4", "L5", "L6", "L8", "L9", "L10")),
        NetworkNode("Ampasambazaha", 0.35f, 0.48f, true, listOf("L1", "L2", "L3", "L4", "L6", "L7", "L9")),
        NetworkNode("Gare FCE", 0.58f, 0.48f, true, listOf("L3", "L6", "L9")),
        NetworkNode("Tanambao", 0.45f, 0.58f, false, listOf("L1", "L6")),
        NetworkNode("Antarandolo", 0.62f, 0.60f, true, listOf("L1", "L3", "L5", "L6", "L8")),
        NetworkNode("Kianjasoa", 0.75f, 0.68f, false, listOf("L1", "L3", "L5", "L8")),
        NetworkNode("Andrainjato Université", 0.88f, 0.78f, true, listOf("L1", "L5", "L8")),
        NetworkNode("Tambohobe (CHU)", 0.18f, 0.40f, true, listOf("L2", "L5", "L7")),
        NetworkNode("Sahalava", 0.10f, 0.52f, false, listOf("L2", "L7")),
        NetworkNode("Isaha", 0.15f, 0.70f, false, listOf("L4", "L9")),
        NetworkNode("Talatamaty", 0.28f, 0.78f, false, listOf("L4", "L9")),
        NetworkNode("Ambatomena", 0.38f, 0.72f, false, listOf("L2", "L6", "L7", "L8")),
        NetworkNode("Beravina", 0.48f, 0.82f, false, listOf("L2", "L6", "L8")),
        NetworkNode("Mahamanina", 0.60f, 0.90f, false, listOf("L2", "L8")),
        NetworkNode("Ankofafa", 0.80f, 0.48f, false, listOf("L3", "L7"))
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
