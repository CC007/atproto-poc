package com.github.cc007.blueart.kolostyles.api

import com.github.cc007.blueart.kolostyles.generator.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.parser.StyleParserHook
import com.github.cc007.blueart.kolostyles.utility.StyleUtilityDefinition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KoloStylesApiTest {

    @Test
    fun emptyFactoryProvidesNoDefinitionsOrHooks() {
        val api = KoloStylesApi.empty()

        assertTrue(api.utilityDefinitions.isEmpty())
        assertTrue(api.parserHooks.isEmpty())
        assertTrue(api.generatorHooks.isEmpty())
    }

    @Test
    fun apiCarriesProvidedDefinitionsAndHooks() {
        val definition = StyleUtilityDefinition(token = "m-4", cssDeclaration = "margin: 1rem;")
        val parserHook = StyleParserHook { definition }
        val generatorHook = StyleGeneratorHook { parsed -> parsed.cssDeclaration }

        val api = KoloStylesApi(
            utilityDefinitions = listOf(definition),
            parserHooks = listOf(parserHook),
            generatorHooks = listOf(generatorHook)
        )

        assertEquals(listOf(definition), api.utilityDefinitions)
        assertEquals(1, api.parserHooks.size)
        assertEquals(1, api.generatorHooks.size)
    }
}

