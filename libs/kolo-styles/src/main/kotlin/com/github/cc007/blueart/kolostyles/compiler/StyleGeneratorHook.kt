package com.github.cc007.blueart.kolostyles.compiler

import kotlinx.css.CssBuilder

/**
 * Produces CSS output for a parsed token when supported.
 */
fun interface StyleGeneratorHook {
    fun generate(token: Token, builder: CssBuilder): Boolean
}
