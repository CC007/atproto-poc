package com.github.cc007.blueart.kolostyles.api

import com.github.cc007.blueart.kolostyles.generator.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.parser.StyleParserHook
import com.github.cc007.blueart.kolostyles.utility.StyleUtilityDefinition

/**
 * Minimal API surface for wiring app->library usage before full style framework work.
 */
data class KoloStylesApi(
    val utilityDefinitions: List<StyleUtilityDefinition> = emptyList(),
    val parserHooks: List<StyleParserHook> = emptyList(),
    val generatorHooks: List<StyleGeneratorHook> = emptyList()
) {
    companion object {
        fun empty(): KoloStylesApi = KoloStylesApi()
    }
}

