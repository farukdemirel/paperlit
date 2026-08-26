package com.farukdemirel.paperlit

data class LibraryUiState(
    val documentCount: Int = 0,
) {
    val isEmpty: Boolean
        get() = documentCount == 0

    val statusText: String
        get() = if (isEmpty) "Kütüphanem boş" else "$documentCount belge"
}
