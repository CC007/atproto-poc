package com.github.cc007.blueart.kolostyles.utility

import com.github.cc007.blueart.kolostyles.generator.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.parser.StyleParserHook
import kotlinx.css.*
import org.springframework.stereotype.Component

private val SPACING_VARIANTS = setOf("hover", "focus", "focus-visible", "active", "visited")
private val SPACING_MEDIA_VARIANTS = mapOf("sm" to 640, "md" to 768, "lg" to 1024, "xl" to 1280)
private val AUTO_SPACING_UTILITIES = setOf("m", "mt", "mr", "mb", "ml", "mx", "my", "p", "pt", "pr", "pb", "pl", "px", "py")
private val SPACING_TOKEN_PATTERN = Regex("^(m|mt|mr|mb|ml|mx|my|p|pt|pr|pb|pl|px|py)-(\\d+|auto)$")

@Component
class SpacingParserHook : StyleParserHook {
    override fun parse(token: String): StyleUtilityDefinition? {
        val declaration = resolveDeclarationForToken(token) ?: return null
        return StyleUtilityDefinition(token = token, cssDeclaration = declaration)
    }
}

@Component
class SpacingGeneratorHook : StyleGeneratorHook {
    override fun generate(definition: StyleUtilityDefinition, builder: CssBuilder): Boolean {
        if (!isSpacingTokenSupported(definition.token)) {
            return false
        }
        builder.emitSpacingRule(definition.token, definition.cssDeclaration)
        return true
    }
}

private data class SpacingToken(
    val token: String,
    val variants: List<String>,
    val utility: String,
    val value: LinearDimension,
)

private fun parseSpacingToken(token: String): SpacingToken? {
    val parts = token.split(':')
    if (parts.any { it.isBlank() }) {
        return null
    }

    val utilityPart = parts.last()
    val match = SPACING_TOKEN_PATTERN.matchEntire(utilityPart) ?: return null
    val utility = match.groupValues[1]
    val rawValue = match.groupValues[2]
    if (utility !in AUTO_SPACING_UTILITIES) {
        return null
    }
    val value = if (rawValue == "auto") LinearDimension.auto else toSpacingDimension(rawValue.toIntOrNull() ?: return null)
    val variants = parts.dropLast(1)

    if (variants.any { it !in SPACING_VARIANTS && it !in SPACING_MEDIA_VARIANTS }) {
        return null
    }
    if (variants.count { it in SPACING_MEDIA_VARIANTS } > 1) {
        return null
    }

    return SpacingToken(
        token = token,
        variants = variants,
        utility = utility,
        value = value,
    )
}


private fun CssBuilder.emitSpacingRule(token: String, declaration: CssBuilder.() -> Unit) {
    val parts = token.split(':')
    val variants = parts.dropLast(1)

    val pseudoSuffix = variants
        .filter { it in SPACING_VARIANTS }
        .joinToString(separator = "") { variant -> ":$variant" }
    val selectorText = ".k-${escapeCssClass(token)}$pseudoSuffix"
    val mediaVariant = variants.firstOrNull { it in SPACING_MEDIA_VARIANTS }

    if (mediaVariant == null) {
        selectorText { declaration() }
    } else {
        media("(min-width: ${SPACING_MEDIA_VARIANTS.getValue(mediaVariant)}px)") {
            selectorText { declaration() }
        }
    }
}

private fun resolveDeclarationForToken(token: String): (CssBuilder.() -> Unit)? {
    val spacingToken = parseSpacingToken(token) ?: return null
    return buildSpacingDeclaration(spacingToken.utility, spacingToken.value)
}

private fun isSpacingTokenSupported(token: String): Boolean {
    return resolveDeclarationForToken(token) != null
}

private fun buildSpacingDeclaration(utility: String, spacingValue: LinearDimension): (CssBuilder.() -> Unit)? {
    return when (utility) {
        "m" -> { { margin = Margin(spacingValue) } }
        "mt" -> { { marginTop = spacingValue } }
        "mr" -> { { marginRight = spacingValue } }
        "mb" -> { { marginBottom = spacingValue } }
        "ml" -> { { marginLeft = spacingValue } }
        "mx" -> { { marginLeft = spacingValue; marginRight = spacingValue } }
        "my" -> { { marginTop = spacingValue; marginBottom = spacingValue } }
        "p" -> { { padding = Padding(spacingValue) } }
        "pt" -> { { paddingTop = spacingValue } }
        "pr" -> { { paddingRight = spacingValue } }
        "pb" -> { { paddingBottom = spacingValue } }
        "pl" -> { { paddingLeft = spacingValue } }
        "px" -> { { paddingLeft = spacingValue; paddingRight = spacingValue } }
        "py" -> { { paddingTop = spacingValue; paddingBottom = spacingValue } }
        else -> null
    }
}

private fun toSpacingDimension(value: Int): LinearDimension {
    return (value.toDouble() / 4.0).rem
}

private fun escapeCssClass(token: String): String = token.replace(":", "\\:")

