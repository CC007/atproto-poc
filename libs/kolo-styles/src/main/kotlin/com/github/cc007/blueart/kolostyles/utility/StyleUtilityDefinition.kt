package com.github.cc007.blueart.kolostyles.utility

import kotlinx.css.CssBuilder

/**
 * Baseline utility representation used by upcoming style parsing/generation tasks.
 */
data class StyleUtilityDefinition(
    val token: String,
    val cssDeclaration: CssBuilder.() -> Unit
)
