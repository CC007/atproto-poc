package com.github.cc007.blueart.kolostyles.compiler.spacing

import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.LinearDimension

internal data class SpacingToken(
    override val raw: String,
    val stateVariants: List<String>,
    val mediaVariant: MediaVariant?,
    val utility: String,
    val value: LinearDimension,
) : Token

data class MediaVariant(val variant: String, val minWidth: LinearDimension)