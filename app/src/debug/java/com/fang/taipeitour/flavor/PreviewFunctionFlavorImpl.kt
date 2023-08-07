package com.fang.taipeitour.flavor

class PreviewFunctionFlavorImpl : PreviewFunctionFlavor {
    override fun invoke(): List<PreviewFunction> {
        return PreviewFunction.values().toList()
    }
}
