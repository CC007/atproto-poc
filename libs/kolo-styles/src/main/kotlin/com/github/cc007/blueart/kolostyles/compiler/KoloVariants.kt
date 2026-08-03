package com.github.cc007.blueart.kolostyles.compiler

import kotlinx.css.LinearDimension
import kotlinx.css.rem

internal val KOLO_STATE_VARIANTS = setOf("hover", "focus", "focus-visible", "active", "visited")

internal val KOLO_MEDIA_VARIANT_MIN_WIDTHS: Map<String, LinearDimension> = mapOf(
    "sm" to 40.rem,
    "md" to 48.rem,
    "lg" to 64.rem,
    "xl" to 80.rem,
    "2xl" to 96.rem,
)
