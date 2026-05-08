package com.github.cc007.blueart.styling

import com.github.cc007.blueart.kolostyles.api.KoloStylesApi
import kotlin.test.Test
import kotlin.test.assertTrue

class KoloStylesModuleWiringTest {

    @Test
    fun appCanInstantiateKoloStylesApiPlaceholder() {
        val api = KoloStylesApi.empty()

        assertTrue(api.utilityDefinitions.isEmpty())
    }
}

