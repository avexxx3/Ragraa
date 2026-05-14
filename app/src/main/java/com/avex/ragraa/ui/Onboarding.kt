package com.avex.ragraa.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.sharedPreferences
import com.avex.ragraa.ui.login.Logo
import java.util.Calendar

@Composable
fun Onboarding(onFinished: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    var captchaKey by remember { mutableStateOf(Datasource.captchaKey) }
    var semId by remember { mutableStateOf(Datasource.semId) }
    var male by remember { mutableStateOf(true) }
    var niqab by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.9f),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                AnimatedContent(
                    targetState = step,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "onboarding_step"
                ) { currentStep ->
                    when (currentStep) {
                        0 -> WelcomeStep { step++ }
                        1 -> CaptchaStep(captchaKey, onKeyChange = { captchaKey = it }) { step++ }
                        2 -> GenderStep(male, niqab, onMaleChange = { male = it }, onNiqabChange = { niqab = it }) { step++ }
                        3 -> SemesterStep(semId, onSemChange = { semId = it }) {
                            Datasource.captchaKey = captchaKey
                            Datasource.semId = semId
                            Datasource.male = male
                            Datasource.niqab = niqab
                            Datasource.showImage = male
                            sharedPreferences.edit()
                                .putString("captchaKey", captchaKey)
                                .putString("semId", semId)
                                .putBoolean("male", male)
                                .putBoolean("niqab", niqab)
                                .putBoolean("showImage", male)
                                .putBoolean("firstTime", false)
                                .apply()
                            Datasource.firstTime = false
                            onFinished()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeStep(onNext: () -> Unit) {
    OnboardingStep(
        title = stringResource(R.string.onboarding_welcome_title),
        description = stringResource(R.string.onboarding_welcome_desc),
        icon = Icons.Default.Info,
        onNext = onNext
    ) {
        Logo(modifier = Modifier.padding(vertical = 16.dp))
    }
}

@Composable
fun CaptchaStep(key: String, onKeyChange: (String) -> Unit, onNext: () -> Unit) {
    OnboardingStep(
        title = stringResource(R.string.onboarding_captcha_title),
        description = stringResource(R.string.onboarding_captcha_desc),
        icon = Icons.Default.Key,
        onNext = onNext
    ) {
        OutlinedTextField(
            value = key,
            onValueChange = onKeyChange,
            label = { Text("2Captcha API Key") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun GenderStep(male: Boolean, niqab: Boolean, onMaleChange: (Boolean) -> Unit, onNiqabChange: (Boolean) -> Unit, onNext: () -> Unit) {
    OnboardingStep(
        title = "Avatar Preference",
        description = "Choose your preferred avatar style for the home screen.",
        icon = Icons.Default.Face,
        onNext = onNext
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = male, onClick = { onMaleChange(true) })
                Text("Male Style", Modifier.clickable { onMaleChange(true) })
                Spacer(Modifier.width(16.dp))
                RadioButton(selected = !male, onClick = { onMaleChange(false) })
                Text("Female Style", Modifier.clickable { onMaleChange(false) })
            }
            
            if (!male) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = niqab, onCheckedChange = onNiqabChange)
                    Text("Niqab Style", Modifier.clickable { onNiqabChange(!niqab) })
                }
            }
        }
    }
}

@Composable
fun SemesterStep(currentSem: String, onSemChange: (String) -> Unit, onFinish: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    OnboardingStep(
        title = stringResource(R.string.onboarding_semester_title),
        description = stringResource(R.string.onboarding_semester_desc),
        icon = Icons.Default.School,
        buttonText = stringResource(R.string.onboarding_finish),
        onNext = onFinish
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = when (currentSem.getOrNull(2)) {
                        '1' -> "Spring "
                        '2' -> "Summer "
                        '3' -> "Fall "
                        else -> "Select Semester "
                    } + currentSem.take(2),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
                for (yearOffset in -1..0) {
                    val year = (currentYear + yearOffset).toString().padStart(2, '0')
                    listOf("1" to "Spring", "2" to "Summer", "3" to "Fall").forEach { (id, name) ->
                        DropdownMenuItem(
                            text = { Text("$name $year") },
                            onClick = {
                                onSemChange("$year$id")
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingStep(
    title: String,
    description: String,
    icon: ImageVector,
    buttonText: String = "Next",
    onNext: () -> Unit,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        content()
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(buttonText)
            if (buttonText == "Next") {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp).size(18.dp)
                )
            }
        }
    }
}
