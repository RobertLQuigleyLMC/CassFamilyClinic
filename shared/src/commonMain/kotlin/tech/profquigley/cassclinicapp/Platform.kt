package tech.profquigley.cassclinicapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform