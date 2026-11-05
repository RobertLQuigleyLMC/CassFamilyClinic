package tech.profquigley.cassfamilyclinic

/** Internal app configuration, not user-editable. */
internal object AppSettings {
    private const val defaultServerURL = "https://localhost:5000/"

    /** Base URL of the backend server. */
    val serverURL: String
        get() = platformServerURL ?: defaultServerURL

    /** MapLibre style used for the base map (OpenFreeMap; free, no API key required). */
    const val mapStyleURL = "https://tiles.openfreemap.org/styles/liberty"
}

/** Platform-specific override for [AppSettings.serverURL], or null to use the default. */
internal expect val platformServerURL: String?
