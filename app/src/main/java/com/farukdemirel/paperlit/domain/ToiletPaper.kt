package com.farukdemirel.paperlit.domain

/**
 * Pilot ürün olan bir tuvalet kâğıdı paketinin kullanıcı tarafından doğrulanmış bilgileri.
 *
 * OCR sonuçları bu modele doğrudan yazılmaz. Önce kullanıcıya gösterilir ve doğrulandıktan
 * sonra bu model oluşturulur.
 */
data class ToiletPaperPackage(
    val brand: String? = null,
    val productName: String? = null,
    val rollCount: Int,
    val sheetsPerRoll: Int? = null,
    val plyCount: Int? = null,
    val sheetWidthMm: Double? = null,
    val sheetLengthMm: Double? = null,
    val lengthPerRollMeters: Double? = null,
) {
    init {
        require(rollCount > 0) { "Rulo sayısı sıfırdan büyük olmalıdır." }
        require(sheetsPerRoll == null || sheetsPerRoll > 0) {
            "Rulo başına yaprak sayısı sıfırdan büyük olmalıdır."
        }
        require(plyCount == null || plyCount > 0) {
            "Kat sayısı sıfırdan büyük olmalıdır."
        }
        require(sheetWidthMm == null || sheetWidthMm > 0.0) {
            "Yaprak eni sıfırdan büyük olmalıdır."
        }
        require(sheetLengthMm == null || sheetLengthMm > 0.0) {
            "Yaprak boyu sıfırdan büyük olmalıdır."
        }
        require(lengthPerRollMeters == null || lengthPerRollMeters > 0.0) {
            "Rulo uzunluğu sıfırdan büyük olmalıdır."
        }
    }

    val totalSheets: Long?
        get() = sheetsPerRoll?.let { rollCount.toLong() * it }

    /**
     * Ambalaj doğrudan rulo uzunluğunu veriyorsa onu kullanır.
     * Aksi halde yaprak sayısı ve yaprak boyundan uzunluğu hesaplar.
     */
    val totalLengthMeters: Double?
        get() = when {
            lengthPerRollMeters != null -> rollCount * lengthPerRollMeters
            sheetsPerRoll != null && sheetLengthMm != null ->
                rollCount * sheetsPerRoll * sheetLengthMm / MILLIMETERS_PER_METER
            else -> null
        }

    val totalSurfaceAreaSquareMeters: Double?
        get() {
            val width = sheetWidthMm ?: return null
            val length = totalLengthMeters ?: return null
            return (width / MILLIMETERS_PER_METER) * length
        }

    /**
     * Kat sayısını yüzey alanına uygular. Bu değer kâğıt miktarı için karşılaştırma
     * göstergesidir; emicilik, kalınlık veya genel kalite puanı değildir.
     */
    val totalLayerAreaSquareMeters: Double?
        get() {
            val area = totalSurfaceAreaSquareMeters ?: return null
            val ply = plyCount ?: return null
            return area * ply
        }

    private companion object {
        const val MILLIMETERS_PER_METER = 1_000.0
    }
}

data class ToiletPaperPriceMetrics(
    val packagePriceKurus: Long,
    val pricePerRollKurus: Double,
    val pricePerHundredSheetsKurus: Double?,
    val pricePerSquareMeterKurus: Double?,
    val pricePerLayerSquareMeterKurus: Double?,
)

object ToiletPaperValueCalculator {
    fun calculate(
        product: ToiletPaperPackage,
        packagePriceKurus: Long,
    ): ToiletPaperPriceMetrics {
        require(packagePriceKurus > 0) { "Paket fiyatı sıfırdan büyük olmalıdır." }

        return ToiletPaperPriceMetrics(
            packagePriceKurus = packagePriceKurus,
            pricePerRollKurus = packagePriceKurus.toDouble() / product.rollCount,
            pricePerHundredSheetsKurus = product.totalSheets?.let {
                packagePriceKurus.toDouble() * 100.0 / it
            },
            pricePerSquareMeterKurus = product.totalSurfaceAreaSquareMeters?.let {
                packagePriceKurus.toDouble() / it
            },
            pricePerLayerSquareMeterKurus = product.totalLayerAreaSquareMeters?.let {
                packagePriceKurus.toDouble() / it
            },
        )
    }
}
