package com.github.cc007.blueart.kolostyles.compiler.layout.display

import com.github.cc007.blueart.kolostyles.compiler.MediaVariant
import com.github.cc007.blueart.kolostyles.compiler.layout.LayoutToken
import kotlinx.css.Display

internal data class DisplayToken(
    override val raw: String,
    override val stateVariants: List<String>,
    override val mediaVariant: MediaVariant?,
    val utility: String,
    val displayValue: Display,
) : LayoutToken
