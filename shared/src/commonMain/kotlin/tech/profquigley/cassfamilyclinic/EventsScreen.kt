package tech.profquigley.cassfamilyclinic

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException

private sealed interface EventsState {
    data object Loading : EventsState
    data class Loaded(val events: List<Event>) : EventsState
    data class Failed(val message: String) : EventsState
}

/** Lists upcoming events fetched from the backend's /events endpoint. */
@Composable
fun EventsScreen(onBack: () -> Unit) {
    var state by remember { mutableStateOf<EventsState>(EventsState.Loading) }
    LaunchedEffect(Unit) {
        state = try {
            EventsState.Loaded(EventsApi.fetchEvents())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            EventsState.Failed(e.message ?: e::class.simpleName ?: "Unknown error")
        }
    }
    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TextButton(onClick = onBack) { Text("< Back") }
        Text(Screen.UpcomingEvents.title, style = MaterialTheme.typography.headlineSmall)
        when (val s = state) {
            EventsState.Loading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is EventsState.Failed -> Text("Couldn't load events: ${s.message}")
            is EventsState.Loaded ->
                if (s.events.isEmpty()) {
                    Text("No upcoming events.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(s.events) { EventCard(it) }
                    }
                }
        }
    }
}

@Composable
private fun EventCard(event: Event) {
    val uriHandler = LocalUriHandler.current
    val venue = event.venue
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            event.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { uriHandler.openUri(event.url) },
        )
        Text(
            "Start: ${formatEventDateTime(event.startDate)}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Text(
            "End: ${formatEventDateTime(event.endDate)}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        val address = venue?.address?.takeIf { it.isNotBlank() }
        val cityLine = venue?.cityLine
        if (address != null || cityLine != null) {
            Spacer(Modifier.height(12.dp))
            address?.let { Text(it) }
            cityLine?.let { Text(it) }
        }
    }
}
