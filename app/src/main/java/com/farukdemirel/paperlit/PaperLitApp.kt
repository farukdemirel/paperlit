package com.farukdemirel.paperlit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farukdemirel.paperlit.domain.ToiletPaperPackage
import com.farukdemirel.paperlit.domain.ToiletPaperPriceMetrics
import com.farukdemirel.paperlit.domain.ToiletPaperValueCalculator
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

@Composable
fun PaperLitApp() {
    var brand by rememberSaveable { mutableStateOf("Örnek Marka") }
    var priceTl by rememberSaveable { mutableStateOf("240") }
    var rollCount by rememberSaveable { mutableStateOf("16") }
    var sheetsPerRoll by rememberSaveable { mutableStateOf("150") }
    var plyCount by rememberSaveable { mutableStateOf("3") }
    var sheetWidthMm by rememberSaveable { mutableStateOf("95") }
    var sheetLengthMm by rememberSaveable { mutableStateOf("120") }
    var metrics by remember { mutableStateOf<ToiletPaperPriceMetrics?>(null) }
    var totalSheets by remember { mutableStateOf<Long?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    MaterialTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "PaperLit",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Tuvalet kâğıdı karşılaştırması",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Ambalajdan okunan bilgileri kontrol edip hesaplayın.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                TextInput("Marka", brand, { brand = it })
                DecimalInput("Paket fiyatı (TL)", priceTl, { priceTl = it })
                IntegerInput("Rulo sayısı", rollCount, { rollCount = it })
                IntegerInput("Rulo başına yaprak", sheetsPerRoll, { sheetsPerRoll = it })
                IntegerInput("Kat sayısı", plyCount, { plyCount = it })
                DecimalInput("Yaprak eni (mm)", sheetWidthMm, { sheetWidthMm = it })
                DecimalInput("Yaprak boyu (mm)", sheetLengthMm, { sheetLengthMm = it })

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        try {
                            val product = ToiletPaperPackage(
                                brand = brand.trim().ifBlank { null },
                                rollCount = rollCount.toRequiredPositiveInt("Rulo sayısı"),
                                sheetsPerRoll = sheetsPerRoll.toOptionalPositiveInt("Yaprak sayısı"),
                                plyCount = plyCount.toOptionalPositiveInt("Kat sayısı"),
                                sheetWidthMm = sheetWidthMm.toOptionalPositiveDouble("Yaprak eni"),
                                sheetLengthMm = sheetLengthMm.toOptionalPositiveDouble("Yaprak boyu"),
                            )
                            val priceKurus = priceTl.toPriceKurus()
                            metrics = ToiletPaperValueCalculator.calculate(product, priceKurus)
                            totalSheets = product.totalSheets
                            errorMessage = null
                        } catch (error: IllegalArgumentException) {
                            metrics = null
                            totalSheets = null
                            errorMessage = error.message
                        }
                    },
                ) {
                    Text("Hesapla")
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                metrics?.let {
                    ResultCard(
                        brand = brand.trim(),
                        totalSheets = totalSheets,
                        metrics = it,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Sürüm ${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun TextInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
    )
}

@Composable
private fun IntegerInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Composable
private fun DecimalInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}

@Composable
private fun ResultCard(
    brand: String,
    totalSheets: Long?,
    metrics: ToiletPaperPriceMetrics,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "Hesap sonucu",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            if (brand.isNotBlank()) {
                Text("Marka: $brand")
            }
            totalSheets?.let { Text("Toplam yaprak: ${formatInteger(it)}") }
            Text("Rulo başına: ${formatKurus(metrics.pricePerRollKurus)}")
            metrics.pricePerHundredSheetsKurus?.let {
                Text("100 yaprak: ${formatKurus(it)}")
            }
            metrics.pricePerSquareMeterKurus?.let {
                Text("1 m²: ${formatKurus(it)}")
            }
            metrics.pricePerLayerSquareMeterKurus?.let {
                Text("Kat eşdeğerli 1 m²: ${formatKurus(it)}")
            }
            Text(
                text = "Kat eşdeğeri bir kalite puanı değildir.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private fun String.toRequiredPositiveInt(fieldName: String): Int =
    trim().toIntOrNull()?.takeIf { it > 0 }
        ?: throw IllegalArgumentException("$fieldName geçerli ve sıfırdan büyük olmalıdır.")

private fun String.toOptionalPositiveInt(fieldName: String): Int? {
    if (isBlank()) return null
    return toRequiredPositiveInt(fieldName)
}

private fun String.toOptionalPositiveDouble(fieldName: String): Double? {
    if (isBlank()) return null
    return normalizedDecimal().toDoubleOrNull()?.takeIf { it > 0.0 }
        ?: throw IllegalArgumentException("$fieldName geçerli ve sıfırdan büyük olmalıdır.")
}

private fun String.toPriceKurus(): Long {
    val value = normalizedDecimal().toBigDecimalOrNull()
        ?: throw IllegalArgumentException("Paket fiyatı geçerli olmalıdır.")
    require(value > BigDecimal.ZERO) { "Paket fiyatı sıfırdan büyük olmalıdır." }
    return value
        .movePointRight(2)
        .setScale(0, RoundingMode.HALF_UP)
        .longValueExact()
}

private fun String.normalizedDecimal(): String = trim().replace(',', '.')

private val turkishLocale = Locale("tr", "TR")

private fun formatKurus(kurus: Double): String =
    String.format(turkishLocale, "%.2f TL", kurus / 100.0)

private fun formatInteger(value: Long): String =
    String.format(turkishLocale, "%,d", value)

@Preview(showBackground = true)
@Composable
private fun PaperLitAppPreview() {
    PaperLitApp()
}
