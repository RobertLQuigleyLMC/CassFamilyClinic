package tech.profquigley.cassfamilyclinic

import androidx.compose.runtime.Composable

/** Calls [onBack] when the system back button/gesture is used, while [enabled]. */
@Composable
expect fun PlatformBackHandler(enabled: Boolean = true, onBack: () -> Unit)
