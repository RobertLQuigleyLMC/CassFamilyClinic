package tech.profquigley.cassfamilyclinic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cassfamilyclinic.shared.generated.resources.Res
import cassfamilyclinic.shared.generated.resources.montserrat_bold
import cassfamilyclinic.shared.generated.resources.montserrat_medium
import cassfamilyclinic.shared.generated.resources.montserrat_regular
import cassfamilyclinic.shared.generated.resources.montserrat_semibold
import cassfamilyclinic.shared.generated.resources.poppins_bold
import cassfamilyclinic.shared.generated.resources.poppins_medium
import cassfamilyclinic.shared.generated.resources.poppins_regular
import cassfamilyclinic.shared.generated.resources.poppins_semibold
import org.jetbrains.compose.resources.Font

/** Brand colors taken from cassfamilyclinic.org and the clinic's logo. */
internal object CassColors {
    /** Section backgrounds and secondary buttons on the website. */
    val Navy = Color(0xFF223661)
    /** Primary buttons and links on the website. */
    val Blue = Color(0xFF307096)
    /** The adult figures and rule in the logo. */
    val Teal = Color(0xFF00867D)
    /** The child figures in the logo. */
    val DeepTeal = Color(0xFF006271)
    /** Light section background on the website. */
    val Mist = Color(0xFFEEF5F6)
    /** Body text on the website. */
    val Text = Color(0xFF404041)
    /** Card borders on the website. */
    val Border = Color(0xFFDDDDDD)
}

private val cassColorScheme = lightColorScheme(
    primary = CassColors.Blue,
    onPrimary = Color.White,
    primaryContainer = CassColors.Mist,
    onPrimaryContainer = CassColors.Navy,
    secondary = CassColors.Navy,
    onSecondary = Color.White,
    tertiary = CassColors.Teal,
    onTertiary = Color.White,
    background = CassColors.Mist,
    onBackground = CassColors.Text,
    surface = Color.White,
    onSurface = CassColors.Text,
    surfaceVariant = CassColors.Mist,
    onSurfaceVariant = CassColors.Text,
    outline = CassColors.Border,
)

/** The website sets headings and buttons in Poppins and body text in Montserrat. */
@Composable
private fun cassTypography(): Typography {
    val poppins = FontFamily(
        Font(Res.font.poppins_regular, FontWeight.Normal),
        Font(Res.font.poppins_medium, FontWeight.Medium),
        Font(Res.font.poppins_semibold, FontWeight.SemiBold),
        Font(Res.font.poppins_bold, FontWeight.Bold),
    )
    val montserrat = FontFamily(
        Font(Res.font.montserrat_regular, FontWeight.Normal),
        Font(Res.font.montserrat_medium, FontWeight.Medium),
        Font(Res.font.montserrat_semibold, FontWeight.SemiBold),
        Font(Res.font.montserrat_bold, FontWeight.Bold),
    )
    val base = Typography()
    fun TextStyle.heading(weight: FontWeight = FontWeight.SemiBold) =
        copy(fontFamily = poppins, fontWeight = weight, color = CassColors.Navy)
    return base.copy(
        displayLarge = base.displayLarge.heading(FontWeight.Bold),
        displayMedium = base.displayMedium.heading(FontWeight.Bold),
        displaySmall = base.displaySmall.heading(FontWeight.Bold),
        headlineLarge = base.headlineLarge.heading(FontWeight.Bold),
        headlineMedium = base.headlineMedium.heading(),
        headlineSmall = base.headlineSmall.heading(),
        titleLarge = base.titleLarge.heading(),
        titleMedium = base.titleMedium.heading(),
        titleSmall = base.titleSmall.heading(FontWeight.Medium),
        bodyLarge = base.bodyLarge.copy(fontFamily = montserrat),
        bodyMedium = base.bodyMedium.copy(fontFamily = montserrat),
        bodySmall = base.bodySmall.copy(fontFamily = montserrat),
        labelLarge = base.labelLarge.copy(fontFamily = poppins, fontWeight = FontWeight.SemiBold),
        labelMedium = base.labelMedium.copy(fontFamily = poppins, fontWeight = FontWeight.Medium),
        labelSmall = base.labelSmall.copy(fontFamily = poppins, fontWeight = FontWeight.Medium),
    )
}

/** The website's buttons and cards use small, squared-off corners. */
private val cassShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(6.dp),
    large = RoundedCornerShape(8.dp),
    extraLarge = RoundedCornerShape(12.dp),
)

@Composable
fun CassTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = cassColorScheme,
        typography = cassTypography(),
        shapes = cassShapes,
        content = content,
    )
}

/** Navy band behind the status bar, matching the website's top bar; system icons are light on it. */
@Composable
internal fun StatusBarBand() {
    Box(
        Modifier
            .fillMaxWidth()
            .background(CassColors.Navy)
            .windowInsetsPadding(WindowInsets.statusBars),
    )
}

/**
 * Common layout for the screens reached from the main menu: a navy header with a Back button
 * and the screen title, a teal rule echoing the logo, then [content] on the light background.
 */
@Composable
internal fun ScreenScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(CassColors.Navy)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 8.dp, end = 24.dp, bottom = 16.dp),
        ) {
            TextButton(onClick = onBack) {
                Text("‹ Back", color = Color.White, style = MaterialTheme.typography.labelLarge)
            }
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
        Spacer(Modifier.fillMaxWidth().height(4.dp).background(CassColors.Teal))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(start = 24.dp, end = 24.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content,
        )
    }
}
