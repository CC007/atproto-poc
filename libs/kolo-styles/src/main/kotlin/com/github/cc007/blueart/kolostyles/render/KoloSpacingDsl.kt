package com.github.cc007.blueart.kolostyles.render

private fun KoloScope.recordSpacing(prefix: String, value: Int) {
    require(value >= 0) { "Spacing utilities require non-negative values" }
    recordBase("$prefix-$value")
}

private fun KoloVariantScope.recordSpacing(prefix: String, value: Int) {
    require(value >= 0) { "Spacing utilities require non-negative values" }
    recordBase("$prefix-$value")
}

fun KoloScope.m(value: Int) = recordSpacing("m", value)
fun KoloScope.mt(value: Int) = recordSpacing("mt", value)
fun KoloScope.mr(value: Int) = recordSpacing("mr", value)
fun KoloScope.mb(value: Int) = recordSpacing("mb", value)
fun KoloScope.ml(value: Int) = recordSpacing("ml", value)
fun KoloScope.mx(value: Int) = recordSpacing("mx", value)
fun KoloScope.my(value: Int) = recordSpacing("my", value)
fun KoloScope.p(value: Int) = recordSpacing("p", value)
fun KoloScope.pt(value: Int) = recordSpacing("pt", value)
fun KoloScope.pr(value: Int) = recordSpacing("pr", value)
fun KoloScope.pb(value: Int) = recordSpacing("pb", value)
fun KoloScope.pl(value: Int) = recordSpacing("pl", value)
fun KoloScope.px(value: Int) = recordSpacing("px", value)
fun KoloScope.py(value: Int) = recordSpacing("py", value)

fun KoloVariantScope.m(value: Int) = recordSpacing("m", value)
fun KoloVariantScope.mt(value: Int) = recordSpacing("mt", value)
fun KoloVariantScope.mr(value: Int) = recordSpacing("mr", value)
fun KoloVariantScope.mb(value: Int) = recordSpacing("mb", value)
fun KoloVariantScope.ml(value: Int) = recordSpacing("ml", value)
fun KoloVariantScope.mx(value: Int) = recordSpacing("mx", value)
fun KoloVariantScope.my(value: Int) = recordSpacing("my", value)
fun KoloVariantScope.p(value: Int) = recordSpacing("p", value)
fun KoloVariantScope.pt(value: Int) = recordSpacing("pt", value)
fun KoloVariantScope.pr(value: Int) = recordSpacing("pr", value)
fun KoloVariantScope.pb(value: Int) = recordSpacing("pb", value)
fun KoloVariantScope.pl(value: Int) = recordSpacing("pl", value)
fun KoloVariantScope.px(value: Int) = recordSpacing("px", value)
fun KoloVariantScope.py(value: Int) = recordSpacing("py", value)

