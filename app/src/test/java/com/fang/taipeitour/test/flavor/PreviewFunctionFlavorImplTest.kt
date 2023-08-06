package com.fang.taipeitour.test.flavor

import com.fang.taipeitour.flavor.PreviewFunction
import com.fang.taipeitour.flavor.PreviewFunctionFlavor
import com.fang.taipeitour.flavor.PreviewFunctionFlavorImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PreviewFunctionFlavorImplTest {

    private lateinit var flavorImpl: PreviewFunctionFlavorImpl

    @Before
    fun setUp() {
        flavorImpl = PreviewFunctionFlavorImpl()
    }

    @Test
    fun testPreviewFunctionFlavorImpl() {
        val mockPreviewFunctions = PreviewFunction.values().toList()
        val mockFlavor = mockk<PreviewFunctionFlavor>()
        every { mockFlavor.invoke() } returns mockPreviewFunctions

        flavorImpl = PreviewFunctionFlavorImpl()

        val result = flavorImpl.invoke()
        assertEquals(mockPreviewFunctions, result)
    }
}