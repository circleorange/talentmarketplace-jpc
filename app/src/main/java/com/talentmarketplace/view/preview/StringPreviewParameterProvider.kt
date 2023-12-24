package com.talentmarketplace.view.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class StringPreviewParameterProvider: PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("Preview String", "Preview String")
}