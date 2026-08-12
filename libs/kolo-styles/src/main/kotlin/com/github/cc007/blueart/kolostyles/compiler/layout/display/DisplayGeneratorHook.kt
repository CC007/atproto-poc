package com.github.cc007.blueart.kolostyles.compiler.layout.display

import com.github.cc007.blueart.kolostyles.compiler.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.CssBuilder
import kotlinx.css.display
import org.springframework.stereotype.Component

@Component
class DisplayGeneratorHook : StyleGeneratorHook {
    override fun generate(token: Token, builder: CssBuilder): Boolean {
        val displayToken = token as? DisplayToken ?: return false
        return builder.emitDisplayRule(displayToken)
    }

    private fun CssBuilder.emitDisplayRule(token: DisplayToken): Boolean {
        val pseudoSuffix = token.stateVariants.joinToString(separator = "") { variant -> ":$variant" }
        val selectorText = ".k-${escapeCssClass(token.raw)}$pseudoSuffix"

        if (token.mediaVariant == null) {
            selectorText {
                display = token.displayValue
            }
        } else {
            media("(min-width: ${token.mediaVariant.minWidth})") {
                selectorText {
                    display = token.displayValue
                }
            }
        }
        return true
    }

    private fun escapeCssClass(token: String): String = token.replace(":", "\\:")
}
