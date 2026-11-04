package tech.profquigley.cassfamilyclinic

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

internal val eventsJson = Json { ignoreUnknownKeys = true }

/** Client for the backend's events endpoint. */
internal object EventsApi {
    private val client by lazy { HttpClient() }

    /** POSTs to `{serverURL}events` and returns the parsed events. */
    suspend fun fetchEvents(): List<Event> {
        val response = client.post("${AppSettings.serverURL.trimEnd('/')}/events")
        check(response.status.isSuccess()) { "Server returned HTTP ${response.status.value}" }
        return eventsJson.decodeFromString(EventsResponse.serializer(), response.bodyAsText()).events
    }
}
