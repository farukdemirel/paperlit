package com.farukdemirel.paperlit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LibraryUiStateTest {
    @Test
    fun emptyLibraryShowsEmptyMessage() {
        val state = LibraryUiState()

        assertTrue(state.isEmpty)
        assertEquals("Kütüphanem boş", state.statusText)
    }

    @Test
    fun libraryWithDocumentsShowsDocumentCount() {
        val state = LibraryUiState(documentCount = 3)

        assertFalse(state.isEmpty)
        assertEquals("3 belge", state.statusText)
    }
}
