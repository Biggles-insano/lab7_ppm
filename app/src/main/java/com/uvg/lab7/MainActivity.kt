package com.uvg.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NotificationScreen()
            }
        }
    }
}


data class Notification(
    val id: Int,
    val type: NotificationType,
    val title: String,
    val message: String,
    val date: String
)

enum class NotificationType {
    INFO,
    TRAINING,
    ALERT,
    UPDATE
}

fun generateFakeNotifications(): List<Notification> {
    return List(50) { index ->
        val type = NotificationType.values()[index % 4]
        Notification(
            id = index,
            type = type,
            title = "Ola jomlander ${type.name}",
            message = "ola, soi jomlander",
            date = "19 ago - 2:30 p.m."
        )
    }.shuffled()
}


@Composable
fun NotificationScreen() {
    val notifications = remember { generateFakeNotifications() }
    var selectedFilter by remember { mutableStateOf<NotificationType?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Tipos de notificaciones", style = MaterialTheme.typography.titleLarge)

        FilterSection(selectedFilter) { filter ->
            selectedFilter = if (selectedFilter == filter) null else filter
        }

        Spacer(modifier = Modifier.height(16.dp))

        val filteredNotifications = notifications.filter {
            selectedFilter == null || it.type == selectedFilter
        }

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(filteredNotifications.size) { index ->
                NotificationItem(notification = filteredNotifications[index])
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}



@Composable
fun FilterSection(selectedFilter: NotificationType?, onFilterSelected: (NotificationType) -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
        FilterButton("Informativas", selectedFilter == NotificationType.INFO) {
            onFilterSelected(NotificationType.INFO)
        }
        FilterButton("Capacitaciones", selectedFilter == NotificationType.TRAINING) {
            onFilterSelected(NotificationType.TRAINING)
        }
        FilterButton("Alertas", selectedFilter == NotificationType.ALERT) {
            onFilterSelected(NotificationType.ALERT)
        }
        FilterButton("Actualizaciones", selectedFilter == NotificationType.UPDATE) {
            onFilterSelected(NotificationType.UPDATE)
        }
    }
}

@Composable
fun FilterButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.LightGray, // Color para el fondo del botón cuando no está seleccionado
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.DarkGray // Color para el texto cuando no está seleccionado
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = text)
    }
}


@Composable
fun NotificationItem(notification: Notification) {
    val icon = when (notification.type) {
        NotificationType.INFO -> Icons.Default.Info
        NotificationType.TRAINING -> Icons.Default.Face
        NotificationType.ALERT -> Icons.Default.Warning
        NotificationType.UPDATE -> Icons.Default.Notifications
    }

    val backgroundColor = when (notification.type) {
        NotificationType.INFO -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        NotificationType.TRAINING -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
        NotificationType.ALERT -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        NotificationType.UPDATE -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
    }

    val iconTint = when (notification.type) {
        NotificationType.INFO -> MaterialTheme.colorScheme.primary
        NotificationType.TRAINING -> MaterialTheme.colorScheme.secondary
        NotificationType.ALERT -> MaterialTheme.colorScheme.error
        NotificationType.UPDATE -> MaterialTheme.colorScheme.tertiary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = notification.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = notification.message, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = notification.date, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            }
        }
    }
}
