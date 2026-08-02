package com.github.cc007.blueart.kolostyles.compiler.spacing

import com.github.cc007.blueart.kolostyles.compiler.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.*
import org.springframework.stereotype.Component

@Component
class SpacingGeneratorHook : StyleGeneratorHook {
    override fun generate(token: Token, builder: CssBuilder): Boolean {
        val spacingToken = token as? SpacingToken ?: return false
        return builder.emitSpacingRule(spacingToken)
    }

    private fun CssBuilder.emitSpacingRule(token: SpacingToken): Boolean {
        val pseudoSuffix = token.stateVariants.joinToString(separator = "") { variant -> ":$variant" }
        val selectorText = ".k-${escapeCssClass(token.raw)}$pseudoSuffix"
        val declaration = buildSpacingDeclaration(token.utility, token.value) ?: return false

        if (token.mediaVariant == null) {
            selectorText { declaration() }
        } else {
            media("(min-width: ${token.mediaVariant.minWidth})") {
                selectorText { declaration() }
            }
        }
        return true
    }

    private fun escapeCssClass(token: String): String = token.replace(":", "\\:")

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
}