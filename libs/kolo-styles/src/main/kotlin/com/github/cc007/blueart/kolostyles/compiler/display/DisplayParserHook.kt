package com.github.cc007.blueart.kolostyles.compiler.display

import com.github.cc007.blueart.kolostyles.compiler.KOLO_MEDIA_VARIANT_MIN_WIDTHS
import com.github.cc007.blueart.kolostyles.compiler.KOLO_STATE_VARIANTS
import com.github.cc007.blueart.kolostyles.compiler.StyleParserHook
import com.github.cc007.blueart.kolostyles.compiler.Token
import kotlinx.css.Display
import org.springframework.stereotype.Component

private val DISPLAY_MEDIA_VARIANTS = KOLO_MEDIA_VARIANT_MIN_WIDTHS
    .mapValues { (name, value) -> MediaVariant(name, value) }
private val DISPLAY_UTILITY_VALUES: Map<String, Display> = linkedMapOf(
    "block" to Display.block,
    "inline" to Display.inline,
    "inline-block" to Display.inlineBlock,
    "flow-root" to Display.flowRoot,
    "flex" to Display.flex,
    "inline-flex" to Display.inlineFlex,
    "grid" to Display.grid,
    "inline-grid" to Display.inlineGrid,
    "contents" to Display.contents,
    "list-item" to Display.listItem,
    "hidden" to Display.none,
    "table" to Display.table,
    "inline-table" to Display.inlineTable,
    "table-caption" to Display.tableCaption,
    "table-cell" to Display.tableCell,
    "table-column" to Display.tableColumn,
    "table-column-group" to Display.tableColumnGroup,
    "table-header-group" to Display.tableHeaderGroup,
    "table-row-group" to Display.tableRowGroup,
    "table-row" to Display.tableRow,
    "table-footer-group" to Display.tableFooterGroup,
)

@Component
class DisplayParserHook : StyleParserHook {
    override fun parse(token: String): Token? {
        val parts = token.split(':')
        if (parts.any { it.isBlank() }) {
            return null
        }

        val utility = parts.last()
        val displayValue = DISPLAY_UTILITY_VALUES[utility] ?: return null
        val variants = parts.dropLast(1)

        if (variants.any { it !in KOLO_STATE_VARIANTS && it !in DISPLAY_MEDIA_VARIANTS }) {
            return null
        }
        val stateVariants = variants.filter { it in KOLO_STATE_VARIANTS }
        val mediaVariants = DISPLAY_MEDIA_VARIANTS.filterKeys { it in variants }
        if (mediaVariants.size > 1) {
            return null
        }

        return DisplayToken(
            raw = token,
            stateVariants = stateVariants,
            mediaVariant = mediaVariants.values.firstOrNull(),
            utility = utility,
            displayValue = displayValue,
        )
    }
}
