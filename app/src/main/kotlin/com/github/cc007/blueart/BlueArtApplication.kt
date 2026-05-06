package com.github.cc007.blueart

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlueArtApplication

fun main(args: Array<String>) {
    runApplication<BlueArtApplication>(*args)
}
