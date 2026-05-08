package com.github.cc007.blueart.kolostyles.parser

import com.github.cc007.blueart.kolostyles.utility.StyleUtilityDefinition

/**
 * Parses one style token into a utility definition when supported.
 */
fun interface StyleParserHook {
    fun parse(token: String): StyleUtilityDefinition?
}

