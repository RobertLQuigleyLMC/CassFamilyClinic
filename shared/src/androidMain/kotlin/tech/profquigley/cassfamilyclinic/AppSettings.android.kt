package tech.profquigley.cassfamilyclinic

/** Build information supplied by the Android app at startup. */
object AndroidBuildInfo {
    var isDebug: Boolean = false
}

// The Android emulator reaches the host machine's localhost at 10.0.2.2.
internal actual val platformServerURL: String?
    get() = if (AndroidBuildInfo.isDebug) "http://10.0.2.2:5000/" else null
