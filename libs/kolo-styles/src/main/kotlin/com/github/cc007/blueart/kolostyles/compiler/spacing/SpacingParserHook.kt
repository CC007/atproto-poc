package com.github.cc007.blueart.kolostyles.compiler.spacing

import com.github.cc007.blueart.kolostyles.compiler.StyleParserHook
import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.LinearDimension
import kotlinx.css.rem
import org.springframework.stereotype.Component

private val SPACING_STATE_VARIANTS = setOf("hover", "focus", "focus-visible", "active", "visited")
private val SPACING_MEDIA_VARIANTS = mapOf("sm" to 40.rem, "md" to 48.rem, "lg" to 64.rem, "xl" to 80.rem, "2xl" to 96.rem)
    .mapValues { (name, value) -> MediaVariant(name, value) }
private val SPACING_UTILITIES = listOf("m", "mt", "mr", "mb", "ml", "mx", "my", "p", "pt", "pr", "pb", "pl", "px", "py")
private val SPACING_TOKEN_PATTERN = Regex("^(${SPACING_UTILITIES.joinToString("|")})-(\\d+|auto)$")

@Component
class SpacingParserHook : StyleParserHook {
    override fun parse(token: String): Token? {
        return parseSpacingToken(token)
    }

    private fun parseSpacingToken(token: String): SpacingToken? {
        val parts = token.split(':')
        if (parts.any { it.isBlank() }) {
            return null
        }

        val utilityPart = parts.last()
        val match = SPACING_TOKEN_PATTERN.matchEntire(utilityPart) ?: return null
        val utility = match.groupValues[1]
        val rawValue = match.groupValues[2]
        val value = if (rawValue == "auto") LinearDimension.auto else toSpacingDimension(rawValue.toIntOrNull() ?: return null)
        val variants = parts.dropLast(1)

        if (variants.any { it !in SPACING_STATE_VARIANTS && it !in SPACING_MEDIA_VARIANTS }) {
            return null
        }
        val stateVariants = variants.filter { it in SPACING_STATE_VARIANTS }
        val mediaVariants = SPACING_MEDIA_VARIANTS.filterKeys { it in variants }
        if (mediaVariants.size > 1) {
            return null
        }

        return SpacingToken(
            raw = token,
            stateVariants = stateVariants,
            mediaVariant = mediaVariants.values.firstOrNull(),
            utility = utility,
            value = value,
        )
    }

    private fun toSpacingDimension(value: Int): LinearDimension {
        return (value.toDouble() / 4.0).rem
    }
}
