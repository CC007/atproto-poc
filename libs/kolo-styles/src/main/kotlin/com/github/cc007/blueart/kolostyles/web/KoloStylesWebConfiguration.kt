package com.github.cc007.blueart.kolostyles.web

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class KoloStylesWebConfiguration {

    @Bean
    open fun koloCssCompiler(): KoloCssCompiler = KoloCssCompiler()
}


