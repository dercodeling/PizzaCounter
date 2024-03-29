package de.dercodeling.pizzacounter.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dercodeling.pizzacounter.R

@OptIn(ExperimentalTextApi::class)
val displayLargeFontFamily = FontFamily(
    Font(
        R.font.sansita_swashed_variable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(800),
            FontVariation.width(30f),
            FontVariation.slant(-6f)
        )
    )
)

@OptIn(ExperimentalTextApi::class)
val displayMediumFontFamily = FontFamily(
    Font(
        R.font.sansita_swashed_variable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400),
            FontVariation.width(30f),
            FontVariation.slant(-6f)
        )
    )
)

val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = displayLargeFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 26.sp,
        letterSpacing = 0.5.sp
    ),
    displayMedium = TextStyle(
        fontFamily = displayMediumFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = 1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
)

@Preview
@Composable
fun TypographyPreview() {
    val prefix = "Lorem ipsum  - "

    Card {
        Column (Modifier.padding(15.dp))
        {
            Text("$prefix Display large", style = typography.displayLarge)
            Text("$prefix Display medium", style = typography.displayMedium)
            Text("$prefix Title large", style = typography.titleLarge)
            Text("$prefix Title medium", style = typography.titleMedium)
            Text("$prefix Title small", style = typography.titleSmall)
            Text("$prefix Body large", style = typography.bodyLarge)
        }
    }
}