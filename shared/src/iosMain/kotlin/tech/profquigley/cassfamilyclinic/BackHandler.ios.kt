package tech.profquigley.cassfamilyclinic

import androidx.compose.runtime.Composable

// iOS has no system back button; navigation uses the on-screen Back button.
@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {}
