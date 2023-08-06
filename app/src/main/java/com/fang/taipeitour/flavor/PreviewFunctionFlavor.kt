package com.fang.taipeitour.flavor

interface PreviewFunctionFlavor : Flavor {
    operator fun invoke(): List<PreviewFunction>
}
