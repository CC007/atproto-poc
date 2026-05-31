package com.github.cc007.blueart.kolostyles.utility

import com.github.cc007.blueart.kolostyles.generator.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.parser.StyleParserHook
import org.springframework.stereotype.Component
import java.math.BigDecimal

private val SPACING_PREFIXES = listOf(
    "m", "mt", "mr", "mb", "ml", "mx", "my",
    "p", "pt", "pr", "pb", "pl", "px", "py",
)

private val SPACING_VARIANTS = setOf("hover", "focus", "focus-visible", "active", "visited")
private val SPACING_MEDIA_VARIANTS = mapOf("sm" to 640, "md" to 768, "lg" to 1024, "xl" to 1280)
private val SPACING_STEPS = (0..16).toList()

internal fun spacingUtilityDefinitions(): List<StyleUtilityDefinition> {
    return SPACING_PREFIXES.flatMap { prefix ->
        SPACING_STEPS.map { value ->
            val token = "$prefix-$value"
            StyleUtilityDefinition(
                token = token,
                cssDeclaration = buildSpacingDeclaration(prefix, value) ?: ""
            )
        }
    }
}

@Component
class SpacingParserHook : StyleParserHook {
    override fun parse(token: String): StyleUtilityDefinition? {
        if (token == "mx-auto") {
            return StyleUtilityDefinition(
                token = token,
                cssDeclaration = "margin-left: auto; margin-right: auto;"
            )
        }

        return parseSpacingToken(token)?.let { spacingToken ->
            StyleUtilityDefinition(
                token = token,
                cssDeclaration = buildSpacingDeclaration(spacingToken.utility, spacingToken.value) ?: return null
            )
        }
    }
}

@Component
class SpacingGeneratorHook : StyleGeneratorHook {
    override fun generate(definition: StyleUtilityDefinition): String? {
        return buildSpacingRule(definition.token, definition.cssDeclaration)
    }
}

private data class SpacingToken(
    val token: String,
    val variants: List<String>,
    val utility: String,
    val value: Int,
)

private fun parseSpacingToken(token: String): SpacingToken? {
    val parts = token.split(':')
    if (parts.any { it.isBlank() }) {
        return null
    }

    val utilityPart = parts.last()
    val match = SPACING_TOKEN_PATTERN.matchEntire(utilityPart) ?: return null
    val utility = match.groupValues[1]
    val value = match.groupValues[2].toIntOrNull() ?: return null
    val variants = parts.dropLast(1)

    if (variants.any { it !in SPACING_VARIANTS && it !in SPACING_MEDIA_VARIANTS }) {
        return null
    }

    return SpacingToken(
        token = token,
        variants = variants,
        utility = utility,
        value = value,
    )
}


private fun buildSpacingRule(token: String, declaration: String): String {
    val parts = token.split(':')
    val variants = parts.dropLast(1)

    val pseudoSuffix = variants
        .filter { it in SPACING_VARIANTS }
        .joinToString(separator = "") { variant -> ":$variant" }
    val selector = ".k-${escapeCssClass(token)}$pseudoSuffix"
    val rule = "$selector { $declaration }"

    return variants
        .filter { it in SPACING_MEDIA_VARIANTS }
        .fold(rule) { css, variant ->
            "@media (min-width: ${SPACING_MEDIA_VARIANTS.getValue(variant)}px) { $css }"
        }
}

private fun buildSpacingDeclaration(utility: String, value: Int): String? {
    val spacingValue = formatSpacingValue(value)
    return when (utility) {
        "m" -> "margin: $spacingValue;"
        "mt" -> "margin-top: $spacingValue;"
        "mr" -> "margin-right: $spacingValue;"
        "mb" -> "margin-bottom: $spacingValue;"
        "ml" -> "margin-left: $spacingValue;"
        "mx" -> "margin-left: $spacingValue; margin-right: $spacingValue;"
        "my" -> "margin-top: $spacingValue; margin-bottom: $spacingValue;"
        "p" -> "padding: $spacingValue;"
        "pt" -> "padding-top: $spacingValue;"
        "pr" -> "padding-right: $spacingValue;"
        "pb" -> "padding-bottom: $spacingValue;"
        "pl" -> "padding-left: $spacingValue;"
        "px" -> "padding-left: $spacingValue; padding-right: $spacingValue;"
        "py" -> "padding-top: $spacingValue; padding-bottom: $spacingValue;"
        else -> null
    }
}

private fun formatSpacingValue(value: Int): String {
    if (value == 0) {
        return "0"
    }

    return BigDecimal(value)
        .multiply(BigDecimal("0.25"))
        .stripTrailingZeros()
        .toPlainString() + "rem"
}

private fun escapeCssClass(token: String): String = token.replace(":", "\\:")

private val SPACING_TOKEN_PATTERN = Regex("^(m|mt|mr|mb|ml|mx|my|p|pt|pr|pb|pl|px|py)-(\\d+)$")




