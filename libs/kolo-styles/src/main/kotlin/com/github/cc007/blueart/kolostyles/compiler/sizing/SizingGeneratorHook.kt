package com.github.cc007.blueart.kolostyles.compiler.sizing

import com.github.cc007.blueart.kolostyles.compiler.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.*
import org.springframework.stereotype.Component

@Component
class SizingGeneratorHook : StyleGeneratorHook {
    override fun generate(token: Token, builder: CssBuilder): Boolean {
        val sizingToken = token as? SizingToken ?: return false
        return builder.emitSizingRule(sizingToken)
    }

    private fun CssBuilder.emitSizingRule(token: SizingToken): Boolean {
        val pseudoSuffix = token.stateVariants.joinToString(separator = "") { variant -> ":$variant" }
        val selectorText = ".k-${escapeCssClass(token.raw)}$pseudoSuffix"
        val declaration = buildSizingDeclaration(token) ?: return false

        val mediaVariant = token.mediaVariant
        if (mediaVariant == null) {
            selectorText { declaration() }
        } else {
            media("(min-width: ${mediaVariant.minWidth})") {
                selectorText { declaration() }
            }
        }
        return true
    }

    private fun buildSizingDeclaration(token: SizingToken): (CssBuilder.() -> Unit)? {
        return when (token.utility) {
            "w" -> { { width = token.value } }
            "h" -> { { height = token.value } }
            "min-w" -> { { minWidth = token.value } }
            "max-w" -> { { maxWidth = token.value } }
            "min-h" -> { { minHeight = token.value } }
            "max-h" -> { { maxHeight = token.value } }
            "size" -> { { width = token.value; height = token.value } }
            else -> null
        }
    }

    private fun escapeCssClass(token: String): String = token.replace(":", "\\:")
}
