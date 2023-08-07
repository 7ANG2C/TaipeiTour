package com.fang.taipeitour.flavor

/**
 * 新功能搶先看
 */
interface PreviewFunctionFlavor : Flavor {
    operator fun invoke(): List<PreviewFunction>
}
