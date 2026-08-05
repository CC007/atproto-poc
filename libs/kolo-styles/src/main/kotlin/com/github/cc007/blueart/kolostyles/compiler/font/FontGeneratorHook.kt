package com.github.cc007.blueart.kolostyles.compiler.font

import com.github.cc007.blueart.kolostyles.compiler.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.CssBuilder
import kotlinx.css.fontFamily
import kotlinx.css.fontSize
import kotlinx.css.fontWeight
import org.springframework.stereotype.Component

@Component
class FontGeneratorHook : StyleGeneratorHook {
    override fun generate(token: Token, builder: CssBuilder): Boolean {
        return when (token) {
            is FontFamilyToken -> builder.emitFontRule(token) {
                fontFamily = token.fontFamilyValue
            }

            is FontSizeToken -> builder.emitFontRule(token) {
                // TODO: Add line-height, like in TailwindCSS
                fontSize = token.fontSizeValue
            }

            is FontWeightToken -> builder.emitFontRule(token) {
                fontWeight = token.fontWeightValue
            }

            else -> false
        }
    }

    private fun CssBuilder.emitFontRule(token: FontToken, declaration: CssBuilder.() -> Unit): Boolean {
        val pseudoSuffix = token.stateVariants.joinToString(separator = "") { variant -> ":$variant" }
        val selectorText = ".k-${escapeCssClass(token.raw)}$pseudoSuffix"

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

    private fun escapeCssClass(token: String): String = token.replace(":", "\\:")
}
