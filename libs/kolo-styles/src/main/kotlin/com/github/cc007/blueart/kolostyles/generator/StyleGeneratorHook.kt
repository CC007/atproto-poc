package com.github.cc007.blueart.kolostyles.generator

import com.github.cc007.blueart.kolostyles.utility.StyleUtilityDefinition
import kotlinx.css.CssBuilder

/**
 * Produces CSS output for a parsed utility when supported.
 */
fun interface StyleGeneratorHook {
    fun generate(definition: StyleUtilityDefinition, builder: CssBuilder): Boolean
}
