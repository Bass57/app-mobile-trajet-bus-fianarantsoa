package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.FianarBusData
import com.example.data.model.NetworkNode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NetworkMapView(
    onSelectDeparture: (String) -> Unit,
    onSelectArrival: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val nodes = remember { FianarBusData.NETWORK_NODES }
    var selectedNode by remember { mutableStateOf<NetworkNode?>(nodes.find { it.stopName == "Tsianolondroa" }) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Explanatory header
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Plan Schématique du Réseau",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Touchez un arrêt sur la carte pour voir les lignes et correspondances.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Canvas Map Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E2A27) // Dark schematic chalkboard background
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("network_map_canvas_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF121B19))
                            .pointerInput(nodes) {
                                detectTapGestures { offset ->
                                    val canvasW = size.width
                                    val canvasH = size.height
                                    // Find closest node within 44px radius
                                    var closest: NetworkNode? = null
                                    var minDistance = Float.MAX_VALUE
                                    nodes.forEach { node ->
                                        val nodeX = node.relativeX * canvasW
                                        val nodeY = node.relativeY * canvasH
                                        val dx = offset.x - nodeX
                                        val dy = offset.y - nodeY
                                        val distance = kotlin.math.sqrt(dx * dx + dy * dy)
                                        if (distance < 50f && distance < minDistance) {
                                            minDistance = distance
                                            closest = node
                                        }
                                    }
                                    if (closest != null) {
                                        selectedNode = closest
                                    }
                                }
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw Connecting Transit Corridors
                            fun drawCorridor(fromName: String, toName: String, color: Color, strokeWidth: Float = 4f) {
                                val from = nodes.find { it.stopName == fromName } ?: return
                                val to = nodes.find { it.stopName == toName } ?: return
                                drawLine(
                                    color = color,
                                    start = Offset(from.relativeX * w, from.relativeY * h),
                                    end = Offset(to.relativeX * w, to.relativeY * h),
                                    strokeWidth = strokeWidth,
                                    cap = StrokeCap.Round
                                )
                            }

                            // University line axis (Red)
                            drawCorridor("Ankazobe", "Ambalapaiso", Color(0xFFE57373), 3f)
                            drawCorridor("Ambalapaiso", "Tsianolondroa", Color(0xFFEF5350), 4f)
                            drawCorridor("Tsianolondroa", "Ampasambazaha", Color(0xFFD32F2F), 6f) // Main central corridor
                            drawCorridor("Tsianolondroa", "Gare FCE", Color(0xFF4CAF50), 4f)
                            drawCorridor("Tsianolondroa", "Antarandolo", Color(0xFFEF5350), 5f)
                            drawCorridor("Antarandolo", "Kianjasoa", Color(0xFFD32F2F), 5f)
                            drawCorridor("Kianjasoa", "Andrainjato Université", Color(0xFFD32F2F), 6f)

                            // Haute-Ville axis (Orange)
                            drawCorridor("Haute-Ville (Rova)", "Ambozontany", Color(0xFFFF9800), 4f)
                            drawCorridor("Ambozontany", "Tsianolondroa", Color(0xFFFF9800), 4f)

                            // Health / Hospital axis (Blue/Purple)
                            drawCorridor("Sahalava", "Tambohobe (CHU)", Color(0xFF29B6F6), 4f)
                            drawCorridor("Tambohobe (CHU)", "Tsianolondroa", Color(0xFF42A5F5), 4f)
                            drawCorridor("Tambohobe (CHU)", "Ampasambazaha", Color(0xFFAB47BC), 4f)

                            // South axis (Cyan/Brown)
                            drawCorridor("Ampasambazaha", "Ambatomena", Color(0xFF26C6DA), 4f)
                            drawCorridor("Ambatomena", "Beravina", Color(0xFF00ACC1), 4f)
                            drawCorridor("Beravina", "Mahamanina", Color(0xFFFF7043), 4f)

                            // Gare axis (Green)
                            drawCorridor("Gare FCE", "Antarandolo", Color(0xFF66BB6A), 4f)
                            drawCorridor("Gare FCE", "Ankofafa", Color(0xFF81C784), 4f)

                            // West / Talatamaty (Orange)
                            drawCorridor("Ampasambazaha", "Isaha", Color(0xFFFFA726), 4f)
                            drawCorridor("Isaha", "Talatamaty", Color(0xFFFFB74D), 4f)

                            // Draw All Nodes
                            nodes.forEach { node ->
                                val center = Offset(node.relativeX * w, node.relativeY * h)
                                val isSelected = selectedNode?.stopName == node.stopName

                                if (isSelected) {
                                    // Pulse / Selection ring
                                    drawCircle(
                                        color = Color(0xFFFFD54F),
                                        radius = 16f,
                                        center = center,
                                        style = Stroke(width = 3f)
                                    )
                                }

                                // Outer circle
                                drawCircle(
                                    color = if (node.isMajorHub) Color.White else Color(0xFFCFD8DC),
                                    radius = if (node.isMajorHub) 8f else 5.5f,
                                    center = center
                                )
                                // Inner dot
                                drawCircle(
                                    color = if (node.isMajorHub) Color(0xFF004D40) else Color(0xFF37474F),
                                    radius = if (node.isMajorHub) 4.5f else 3f,
                                    center = center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mini map legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Grand Carrefour Hub",
                                color = Color(0xFFB0BEC5),
                                fontSize = 11.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFCFD8DC))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Arrêt Standard",
                                color = Color(0xFFB0BEC5),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Detail Card for Selected Stop
        selectedNode?.let { node ->
            item {
                val stopObj = FianarBusData.findStopByName(node.stopName)
                val linesPassing = FianarBusData.findLinesPassingByStop(node.stopName)

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("selected_stop_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = node.stopName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (stopObj != null) {
                                        Text(
                                            text = "Zone : ${stopObj.zone}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            if (node.isMajorHub) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = "Hub Central",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        if (stopObj != null && stopObj.description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stopObj.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Lignes desservant cet arrêt (${linesPassing.size}) :",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            linesPassing.forEach { line ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = line.color.copy(alpha = 0.15f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(line.color)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${line.number} (${line.departure} ⇄ ${line.terminus})",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = line.color
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick action buttons for Itinerary
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onSelectDeparture(node.stopName) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Départ",
                                    fontSize = 13.sp
                                )
                            }
                            OutlinedButton(
                                onClick = { onSelectArrival(node.stopName) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Arrivée",
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
