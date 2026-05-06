package com.github.cc007.blueart

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<BlueArtApplication>().with(TestcontainersConfiguration::class).run(*args)
}
