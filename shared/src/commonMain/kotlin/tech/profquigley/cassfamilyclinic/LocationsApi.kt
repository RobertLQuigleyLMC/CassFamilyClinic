package tech.profquigley.cassfamilyclinic

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

/** Client for the backend's locations endpoint. */
internal object LocationsApi {
    private val client by lazy { HttpClient() }

    /** POSTs to `{serverURL}locations` and returns the clinic's trail. */
    suspend fun fetchTrail(): LocationTrail {
        val response = client.post("${AppSettings.serverURL.trimEnd('/')}/locations")
        check(response.status.isSuccess()) { "Server returned HTTP ${response.status.value}" }
        return eventsJson.decodeFromString(LocationsResponse.serializer(), response.bodyAsText()).toLocationTrail()
    }
}
