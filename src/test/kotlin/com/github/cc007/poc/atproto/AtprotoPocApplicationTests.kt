package com.github.cc007.poc.atproto

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class AtprotoPocApplicationTests {

    @Test
    fun contextLoads() {
    }

}
