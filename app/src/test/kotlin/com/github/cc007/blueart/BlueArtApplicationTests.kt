package com.github.cc007.blueart

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class BlueArtApplicationTests {

    @Test
    fun contextLoads() {
    }

}
