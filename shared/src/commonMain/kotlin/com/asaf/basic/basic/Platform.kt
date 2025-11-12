package com.asaf.basic.basic

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform