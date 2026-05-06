package com.github.cc007.blueart.styling

import kotlinx.css.CssBuilder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class CssController {

    @GetMapping("/css/generated/browse.css", produces = ["text/css"])
    @ResponseBody
    fun browseStylesheet(): String = generatedImportCss("/css/browse.css")

    @GetMapping("/css/generated/art.css", produces = ["text/css"])
    @ResponseBody
    fun artStylesheet(): String = generatedImportCss("/css/art.css")

    private fun generatedImportCss(importPath: String): String {
        val bridgeCss = CssBuilder().toString()
        return "@import url('$importPath');\n$bridgeCss"
    }
}



