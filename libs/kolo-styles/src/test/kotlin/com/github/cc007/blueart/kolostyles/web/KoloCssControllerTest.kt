package com.github.cc007.blueart.kolostyles.web

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class KoloCssControllerTest {

    private val controller = KoloCssController(KoloCssCompiler())

    @Test
    fun `kolo stylesheet accepts valid tokens and emits unsupported comments with placeholder hooks`() {
        val response = controller.koloStylesheet(
            version = "abc123",
            kolo = "mt-2;flex;hover:mt-2"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(
            "/* kolo-unsupported: mt-2 *//* kolo-unsupported: flex *//* kolo-unsupported: hover:mt-2 */",
            response.body
        )
    }

    @Test
    fun `kolo stylesheet notes arbitrary value tokens as unparsed comments`() {
        val response = controller.koloStylesheet(
            version = "abc123",
            kolo = "mt-[2px]"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("/* kolo-unparsed: mt-[2px] */", response.body)
    }

    @Test
    fun `kolo stylesheet notes malformed tokens as unparsed comments`() {
        val response = controller.koloStylesheet(
            version = "abc123",
            kolo = "mt- 2"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("/* kolo-unparsed: mt- 2 */", response.body)
    }
}

