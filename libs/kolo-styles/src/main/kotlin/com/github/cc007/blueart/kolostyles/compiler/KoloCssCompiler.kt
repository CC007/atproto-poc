package com.github.cc007.blueart.kolostyles.compiler

import com.github.cc007.blueart.kolostyles.generator.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.parser.StyleParserHook
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

val logger = KotlinLogging.logger {}
/**
 * Compiles canonicalized kolo token lists into deterministic CSS output.
 *
 * Expects tokens to be pre-validated and passed as-is (in the order provided by client).
 * Deduping and sorting happens client-side (BA-022) for link URL caching.
 * Server-side generation just converts the token list into CSS rules.
 *
 * Utility-to-CSS mapping is pluggable via parser/generator hooks.
 */
@Service
class KoloCssCompiler(
    private val parserHooks: List<StyleParserHook> = emptyList(),
    private val generatorHooks: List<StyleGeneratorHook> = emptyList()
) {
    init {
        logger.info { "Parser hooks loaded: ${parserHooks.map { it::class.simpleName }}" }
        logger.info { "Generator hooks loaded: ${generatorHooks.map { it::class.simpleName }}" }
    }

    fun compile(rawKolo: String?): String {
        val tokens = parseTokens(rawKolo)
        return tokens.joinToString(separator = "") { token ->
            if (isMalformedToken(token)) {
                return@joinToString "/* kolo-unparsed: ${escapeCommentBody(token)} */"
            }
            generateViaHooks(token) ?: "/* kolo-unsupported: ${escapeCommentBody(token)} */"
        }
    }

    private fun generateViaHooks(token: String): String? {
        if (parserHooks.isEmpty() || generatorHooks.isEmpty()) {
            return null
        }

        val parsed = parserHooks.firstNotNullOfOrNull { hook -> hook.parse(token) } ?: return null
        return generatorHooks.firstNotNullOfOrNull { hook -> hook.generate(parsed) }
    }

    private fun parseTokens(rawKolo: String?): List<String> {
        return rawKolo
            .orEmpty()
            .split(';')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    private fun isMalformedToken(token: String): Boolean {
        if (token.contains('[') || token.contains(']') || token.contains(';') || token.any { it.isWhitespace() }) {
            return true
        }

        val parts = token.split(':')
        return parts.any { it.isBlank() }
    }

    private fun escapeCommentBody(value: String): String {
        return value
            .replace("*/", "* /")
            .replace("\n", " ")
            .replace("\r", " ")
    }
}
