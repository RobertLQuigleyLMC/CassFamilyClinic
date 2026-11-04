package tech.profquigley.cassfamilyclinic

/** Internal app configuration, not user-editable. */
internal object AppSettings {
    private const val defaultServerURL = "https://localhost:5000/"

    /** Base URL of the backend server. */
    val serverURL: String
        get() = platformServerURL ?: defaultServerURL
}

/** Platform-specific override for [AppSettings.serverURL], or null to use the default. */
internal expect val platformServerURL: String?
