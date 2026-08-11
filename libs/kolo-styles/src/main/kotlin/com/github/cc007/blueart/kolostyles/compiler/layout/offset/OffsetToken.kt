package com.github.cc007.blueart.kolostyles.compiler.layout.offset

import com.github.cc007.blueart.kolostyles.compiler.MediaVariant
import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.LinearDimension

internal data class OffsetToken(
    override val raw: String,
    val stateVariants: List<String>,
    val mediaVariant: MediaVariant?,
    val utility: String,
    val value: LinearDimension,
) : Token
