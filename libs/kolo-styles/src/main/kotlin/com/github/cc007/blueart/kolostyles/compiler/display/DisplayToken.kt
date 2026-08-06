package com.github.cc007.blueart.kolostyles.compiler.display

import com.github.cc007.blueart.kolostyles.compiler.MediaVariant
import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.Display

internal data class DisplayToken(
    override val raw: String,
    val stateVariants: List<String>,
    val mediaVariant: MediaVariant?,
    val utility: String,
    val displayValue: Display,
) : Token
