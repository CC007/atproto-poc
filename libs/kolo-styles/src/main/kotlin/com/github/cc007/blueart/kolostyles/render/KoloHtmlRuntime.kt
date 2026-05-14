package com.github.cc007.blueart.kolostyles.render

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val KOLO_HREF_PLACEHOLDER = "__blueart_kolo_href__"

private data class KoloRenderContext(
    val version: String,
    val classNameMapper: (String) -> String?,
    val tokens: MutableList<String> = mutableListOf(),
)

private object KoloRenderContextHolder {
    private val current = ThreadLocal<KoloRenderContext?>()

    fun <T> withContext(context: KoloRenderContext, block: () -> T): T {
        val previous = current.get()
        current.set(context)
        return try {
            block()
        } finally {
            if (previous == null) {
                current.remove()
            } else {
                current.set(previous)
            }
        }
    }

    fun current(): KoloRenderContext? = current.get()
}

fun renderKoloHtml(
    version: String = defaultKoloVersion(),
    classNameMapper: (String) -> String? = { null },
    block: HTML.() -> Unit,
): String {
    val context = KoloRenderContext(
        version = version,
        classNameMapper = classNameMapper,
    )
    return KoloRenderContextHolder.withContext(context) {
        val html = createHTML().html(block = block)
        html.replace(KOLO_HREF_PLACEHOLDER, buildKoloHref(context))
    }
}

fun HEAD.koloStylesheetLink() {
    val context = KoloRenderContextHolder.current()
    val href = context?.let { KOLO_HREF_PLACEHOLDER } ?: buildKoloHref(
        KoloRenderContext(
            version = defaultKoloVersion(),
            classNameMapper = { null },
        )
    )

    link(rel = "stylesheet", href = href)
}

fun HTMLTag.kolo(block: KoloScope.() -> Unit) {
    val context = KoloRenderContextHolder.current() ?: return
    val emitted = mutableListOf<String>()
    KoloScope(
        sink = { token ->
            emitted += token
            context.tokens += token
        }
    ).block()

    emitted
        .mapNotNull(context.classNameMapper)
        .distinct()
        .forEach { appendClass(it) }
}

class KoloScope internal constructor(
    private val sink: (String) -> Unit,
    private val variants: List<String> = emptyList(),
) {
    val flex: Unit
        get() = record("flex")

    fun mt(value: Int) {
        record("mt-$value")
    }

    fun px(value: Int) {
        record("px-$value")
    }

    val hover: KoloVariantScope
        get() = KoloVariantScope(this, "hover")

    val md: KoloVariantScope
        get() = KoloVariantScope(this, "md")

    val bg: KoloBackgroundScope
        get() = KoloBackgroundScope(this)

    internal fun withVariant(variant: String): KoloScope = KoloScope(sink, variants + variant)

    internal fun record(baseToken: String) {
        val token = if (variants.isEmpty()) {
            baseToken
        } else {
            variants.joinToString(separator = ":", postfix = ":") + baseToken
        }
        sink(token)
    }
}

class KoloVariantScope internal constructor(
    private val parent: KoloScope,
    private val variant: String,
) {
    private fun scoped(): KoloScope = parent.withVariant(variant)

    val flex: Unit
        get() = scoped().flex

    fun mt(value: Int) {
        scoped().mt(value)
    }

    fun px(value: Int) {
        scoped().px(value)
    }

    val bg: KoloBackgroundScope
        get() = scoped().bg
}

class KoloBackgroundScope internal constructor(
    private val scope: KoloScope,
) {
    val sky: KoloShadeScope
        get() = KoloShadeScope(scope, "bg-sky")
}

class KoloShadeScope internal constructor(
    private val scope: KoloScope,
    private val prefix: String,
) {
    operator fun invoke(shade: Int) {
        scope.record("$prefix-$shade")
    }
}

fun canonicalizeKoloTokens(tokens: Iterable<String>): String {
    val canonical = tokens
        .flatMap { raw -> raw.split(';') }
        .map { part -> part.trim() }
        .filter { part -> part.isNotEmpty() }
        .filter { part -> isCanonicalizable(part) }
        .distinct()
        .sortedWith(
            compareBy<String>(
                { tokenGroup(it) },
                { tokenVariantCount(it) },
                { tokenVariantChain(it) },
                { tokenBaseUtility(it) },
                { it },
            )
        )

    return canonical.joinToString(separator = ";")
}

private fun buildKoloHref(context: KoloRenderContext): String {
    val canonical = canonicalizeKoloTokens(context.tokens)
    return "/css/generated/kolo.css?version=${encodeQueryParam(context.version)}&kolo=${encodeQueryParam(canonical)}"
}

private fun defaultKoloVersion(): String {
    return System.getProperty("blueart.build.sha")
        ?: System.getenv("BLUEART_BUILD_SHA")
        ?: "dev"
}

private fun encodeQueryParam(value: String): String {
    return URLEncoder.encode(value, StandardCharsets.UTF_8)
}

private fun isCanonicalizable(token: String): Boolean {
    if (token.contains(';') || token.contains('[') || token.contains(']')) {
        return false
    }
    if (token.any { it.isWhitespace() }) {
        return false
    }
    return token.split(':').none { it.isBlank() }
}

private fun tokenVariantCount(token: String): Int = token.split(':').size - 1

private fun tokenVariantChain(token: String): String {
    val parts = token.split(':')
    return if (parts.size <= 1) "" else parts.dropLast(1).joinToString(":")
}

private fun tokenBaseUtility(token: String): String = token.substringAfterLast(':')

private fun tokenGroup(token: String): String {
    val base = tokenBaseUtility(token)
    return base.substringBefore('-', base)
}

private fun HTMLTag.appendClass(className: String) {
    val existing = attributes["class"].orEmpty().trim()
    attributes["class"] = if (existing.isEmpty()) {
        className
    } else {
        "$existing $className"
    }
}




