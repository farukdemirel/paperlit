package com.farukdemirel.paperlit.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class ToiletPaperValueCalculatorTest {
    @Test
    fun calculatesMetricsFromSheetInformation() {
        val product = ToiletPaperPackage(
            brand = "Örnek",
            productName = "Üç Katlı",
            rollCount = 16,
            sheetsPerRoll = 150,
            plyCount = 3,
            sheetWidthMm = 95.0,
            sheetLengthMm = 120.0,
        )

        val metrics = ToiletPaperValueCalculator.calculate(
            product = product,
            packagePriceKurus = 24_000,
        )

        assertEquals(2_400L, product.totalSheets)
        assertEquals(288.0, product.totalLengthMeters!!, 0.0001)
        assertEquals(27.36, product.totalSurfaceAreaSquareMeters!!, 0.0001)
        assertEquals(82.08, product.totalLayerAreaSquareMeters!!, 0.0001)
        assertEquals(1_500.0, metrics.pricePerRollKurus, 0.0001)
        assertEquals(1_000.0, metrics.pricePerHundredSheetsKurus!!, 0.0001)
        assertEquals(292.3977, metrics.pricePerLayerSquareMeterKurus!!, 0.0001)
    }

    @Test
    fun calculatesAreaFromDeclaredRollLength() {
        val product = ToiletPaperPackage(
            rollCount = 12,
            plyCount = 2,
            sheetWidthMm = 100.0,
            lengthPerRollMeters = 20.0,
        )

        val metrics = ToiletPaperValueCalculator.calculate(
            product = product,
            packagePriceKurus = 12_000,
        )

        assertNull(product.totalSheets)
        assertEquals(240.0, product.totalLengthMeters!!, 0.0001)
        assertEquals(24.0, product.totalSurfaceAreaSquareMeters!!, 0.0001)
        assertEquals(48.0, product.totalLayerAreaSquareMeters!!, 0.0001)
        assertNull(metrics.pricePerHundredSheetsKurus)
        assertEquals(250.0, metrics.pricePerLayerSquareMeterKurus!!, 0.0001)
    }

    @Test
    fun leavesMetricsUnknownWhenPackageOmitsRequiredDimensions() {
        val product = ToiletPaperPackage(
            rollCount = 8,
            sheetsPerRoll = 200,
            plyCount = 3,
        )

        val metrics = ToiletPaperValueCalculator.calculate(
            product = product,
            packagePriceKurus = 10_000,
        )

        assertEquals(1_600L, product.totalSheets)
        assertEquals(625.0, metrics.pricePerHundredSheetsKurus!!, 0.0001)
        assertNull(product.totalSurfaceAreaSquareMeters)
        assertNull(metrics.pricePerSquareMeterKurus)
        assertNull(metrics.pricePerLayerSquareMeterKurus)
    }

    @Test
    fun rejectsInvalidCountsAndPrices() {
        assertThrows(IllegalArgumentException::class.java) {
            ToiletPaperPackage(rollCount = 0)
        }

        val product = ToiletPaperPackage(rollCount = 4)
        assertThrows(IllegalArgumentException::class.java) {
            ToiletPaperValueCalculator.calculate(product, packagePriceKurus = 0)
        }
    }
}
