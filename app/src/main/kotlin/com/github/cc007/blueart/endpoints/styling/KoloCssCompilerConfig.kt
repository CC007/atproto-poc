package com.github.cc007.blueart.endpoints.styling

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KoloCssCompilerConfig {

    @Bean
    fun koloCssCompiler(): KoloCssCompiler = KoloCssCompiler()
}

