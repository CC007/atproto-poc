package com.github.cc007.poc.atproto

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<AtprotoPocApplication>().with(TestcontainersConfiguration::class).run(*args)
}
