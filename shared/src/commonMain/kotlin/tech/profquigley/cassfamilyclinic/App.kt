package tech.profquigley.cassfamilyclinic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class Screen(val title: String) {
    TrackMobileClinic("Track Mobile Clinic"),
    UpcomingEvents("See Upcoming Events"),
    ContactUs("Contact Us"),
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var current by rememberSaveable { mutableStateOf<Screen?>(null) }
            val screen = current
            PlatformBackHandler(enabled = screen != null) { current = null }
            if (screen == null) {
                MainMenu(onSelect = { current = it })
            } else if (screen == Screen.TrackMobileClinic) {
                TrackClinicScreen(onBack = { current = null })
            } else if (screen == Screen.UpcomingEvents) {
                EventsScreen(onBack = { current = null })
            } else {
                PlaceholderScreen(screen, onBack = { current = null })
            }
        }
    }
}

@Composable
fun MainMenu(onSelect: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Text(
            "Cass Family Clinic",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Screen.entries.forEach { screen ->
            Button(
                onClick = { onSelect(screen) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(screen.title)
            }
        }
    }
}

@Composable
fun PlaceholderScreen(screen: Screen, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TextButton(onClick = onBack) { Text("< Back") }
        Text(screen.title, style = MaterialTheme.typography.headlineSmall)
        Text("Coming soon.")
    }
}
