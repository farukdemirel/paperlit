package com.farukdemirel.paperlit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class PaperLitAppTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun productFormCalculatesSamplePackage() {
        composeRule.setContent {
            PaperLitApp()
        }

        composeRule.onNodeWithText("PaperLit").assertIsDisplayed()
        composeRule.onNodeWithText("Tuvalet kâğıdı karşılaştırması").assertIsDisplayed()

        composeRule.onNodeWithText("Hesapla")
            .performScrollTo()
            .performClick()

        composeRule.onNodeWithText("Hesap sonucu")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithText("Toplam yaprak: 2.400").assertIsDisplayed()
        composeRule.onNodeWithText("Rulo başına: 15,00 TL").assertIsDisplayed()
        composeRule.onNodeWithText("100 yaprak: 10,00 TL").assertIsDisplayed()
        composeRule.onNodeWithText("Kat eşdeğerli 1 m²: 2,92 TL").assertIsDisplayed()
    }
}
