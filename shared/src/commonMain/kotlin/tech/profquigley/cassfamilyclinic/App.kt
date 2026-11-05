package tech.profquigley.cassfamilyclinic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cassfamilyclinic.shared.generated.resources.Res
import cassfamilyclinic.shared.generated.resources.cfc_logo
import org.jetbrains.compose.resources.painterResource

enum class Screen(val title: String) {
    TrackMobileClinic("Track Mobile Clinic"),
    UpcomingEvents("See Upcoming Events"),
    ContactUs("Contact Us"),
}

@Composable
@Preview
fun App() {
    CassTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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
    Column(Modifier.fillMaxSize()) {
        StatusBarBand()
        // White header carrying the logo, like the website's masthead.
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(Res.drawable.cfc_logo),
                contentDescription = "Cass Family Clinic. Our Family, Caring For Yours.",
                modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
            )
        }
        Spacer(Modifier.fillMaxWidth().height(4.dp).background(CassColors.Teal))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Screen.entries.forEach { screen ->
                Button(
                    onClick = { onSelect(screen) },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth().height(56.dp),
                ) {
                    Text(screen.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(screen: Screen, onBack: () -> Unit) {
    ScreenScaffold(title = screen.title, onBack = onBack) {
        Text("Coming soon.", style = MaterialTheme.typography.bodyLarge)
    }
}
