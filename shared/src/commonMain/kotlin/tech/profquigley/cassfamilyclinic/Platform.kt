package tech.profquigley.cassfamilyclinic

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform