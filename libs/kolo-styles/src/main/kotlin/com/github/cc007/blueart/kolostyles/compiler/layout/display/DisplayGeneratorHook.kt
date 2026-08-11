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
        return builder.emitVariantRule(
            rawToken = displayToken.raw,
            stateVariants = displayToken.stateVariants,
            mediaVariant = displayToken.mediaVariant,
        ) {
            display = displayToken.displayValue
        }
    }
}
