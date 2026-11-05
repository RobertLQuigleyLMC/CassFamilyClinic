package tech.profquigley.cassfamilyclinic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import org.maplibre.compose.camera.CameraFit
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.value.LineCap
import org.maplibre.compose.expressions.value.LineJoin
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.rememberMapState
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.DpPadding
import org.maplibre.spatialk.geojson.LineString
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration.Companion.seconds

private sealed interface TrackState {
    data object Loading : TrackState
    data class Loaded(val trail: LocationTrail) : TrackState
    data class Failed(val message: String) : TrackState
}

private val refreshInterval = 30.seconds

/**
 * Shows the mobile clinic's recent path from `/locations` as a polyline on a map, refreshing
 * every [refreshInterval] while the screen is open.
 */
@Composable
fun TrackClinicScreen(onBack: () -> Unit) {
    var state by remember { mutableStateOf<TrackState>(TrackState.Loading) }
    LaunchedEffect(Unit) {
        while (true) {
            val result = try {
                TrackState.Loaded(LocationsApi.fetchTrail())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                TrackState.Failed(e.message ?: e::class.simpleName ?: "Unknown error")
            }
            // If a refresh fails, keep showing the last trail that loaded rather than an error.
            if (result is TrackState.Loaded || state !is TrackState.Loaded) state = result
            delay(refreshInterval)
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
        Text(Screen.TrackMobileClinic.title, style = MaterialTheme.typography.headlineSmall)
        when (val s = state) {
            TrackState.Loading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is TrackState.Failed -> Text("Couldn't load the clinic's location: ${s.message}")
            is TrackState.Loaded ->
                if (s.trail.points.isEmpty()) {
                    Text("The mobile clinic hasn't reported its location recently.")
                } else {
                    TrailMap(
                        trail = s.trail,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    )
                }
        }
    }
}

@Composable
private fun TrailMap(trail: LocationTrail, modifier: Modifier = Modifier) {
    val positions = trail.points.map { Position(longitude = it.longitude, latitude = it.latitude) }
    val mapState = rememberMapState(
        baseStyle = BaseStyle.Uri(AppSettings.mapStyleURL),
        initialCameraPosition = CameraPosition(center = positions.last(), zoom = 14.0),
    ) {
        // A polyline needs at least two points. Source data is immutable, so rebuild the source
        // whenever a refresh brings different positions.
        if (positions.size >= 2) key(positions) {
            val line = rememberGeoJsonSource(GeoJsonData.Features(LineString(positions)))
            LineLayer(
                id = "clinic-trail",
                source = line,
                color = const(Color(0xFF1565C0)),
                width = const(4.dp),
                cap = const(LineCap.Round),
                join = const(LineJoin.Round),
            )
        }
        // Marks where the clinic was most recently reported; declared after the line so it draws on top.
        key(positions.last()) {
            val latest = rememberGeoJsonSource(GeoJsonData.Features(Point(positions.last())))
            CircleLayer(
                id = "clinic-latest",
                source = latest,
                color = const(Color(0xFFD32F2F)),
                radius = const(9.dp),
                strokeColor = const(Color.White),
                strokeWidth = const(3.dp),
            )
        }
    }
    // Fit the route once when the map opens; refreshes don't move the camera the user may have panned.
    LaunchedEffect(mapState) {
        if (positions.size >= 2) {
            val fitted = mapState.cameraForCoordinates(
                positions,
                CameraFit(fitPadding = DpPadding(48.dp, 48.dp, 48.dp, 48.dp)),
            )
            // Don't zoom in absurdly far when the clinic has barely moved.
            mapState.setCameraPosition(fitted.copy(zoom = minOf(fitted.zoom, 16.0)))
        }
    }
    MaplibreMap(modifier = modifier, state = mapState)
}
