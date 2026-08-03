package com.github.cc007.blueart.kolostyles.compiler

/**
 * Parses one style token into a typed token when supported.
 */
fun interface StyleParserHook {
    fun parse(token: String): Token?
}
