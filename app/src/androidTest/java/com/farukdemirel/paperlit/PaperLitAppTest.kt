package com.farukdemirel.paperlit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class PaperLitAppTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun emptyLibraryScreenShowsTitleAndStatus() {
        composeRule.setContent {
            PaperLitApp()
        }

        composeRule.onNodeWithText("PaperLit").assertIsDisplayed()
        composeRule.onNodeWithText("Kütüphanem boş").assertIsDisplayed()
        composeRule.onNodeWithText("Sürüm 0.1.0").assertIsDisplayed()
    }
}
