package com.github.cc007.blueart.kolostyles.compiler.font

import com.github.cc007.blueart.kolostyles.compiler.MediaVariant
import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.FontWeight
import kotlinx.css.LinearDimension

internal sealed interface FontToken : Token {
    val stateVariants: List<String>
    val mediaVariant: MediaVariant?
    val utility: String
}

internal data class FontFamilyToken(
    override val raw: String,
    override val stateVariants: List<String>,
    override val mediaVariant: MediaVariant?,
    override val utility: String,
    val fontFamilyValue: String,
) : FontToken

internal data class FontSizeToken(
    override val raw: String,
    override val stateVariants: List<String>,
    override val mediaVariant: MediaVariant?,
    override val utility: String,
    val fontSizeValue: LinearDimension,
) : FontToken

internal data class FontWeightToken(
    override val raw: String,
    override val stateVariants: List<String>,
    override val mediaVariant: MediaVariant?,
    override     val utility: String,
    val fontWeightValue: FontWeight,
) : FontToken
