package com.avex.ragraa

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.avex.ragraa.ui.login.Logo

@Composable
fun About() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Logo()


        Text(
            buildAnnotatedString {
                append("Developed by ")

                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black
                )) {
                    append("Armaghan Atiq")
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            "\nit's been almost a year (circa 2023) thought of making a flex clone and naming it 'Ragraa' as a funny; and it was, albeit for the first few times. ever since then seeing the name or associating with it has filled me with hatred for myself and now the branding is too deep for me to consider changing it now." +
                    "\n\nbuilt for android (kotlin) because i didn't want to make something cross platform just for ios users, and i really really love writing kotlin" +
                    "\n\ni don't want to say much about myself so this is just to fill the space up a bit more. also if you are a recruiter somewhere and seeing this please please consider me for an internship in machine learning i will prove to be useful trust me" +
                    "\n\nthe code is open source and linked below so if you have anything you'd like to add, feel free. the codebase wont be too difficult to understand and i would say a bit intuitive if you have knowledge of how the structure of these applications generally goes.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(0.5f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            for (social in Socials.entries.toTypedArray()) {
                Icon(
                    painterResource(social.id),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clickable { openUrl(social.url) })
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            }
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clickable { openUrl("mailto:avexuchiha@gmail.com") })
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        }

        Spacer(modifier = Modifier.weight(0.5f))
    }
}

fun openUrl(url: String) {
    val intent = Intent(
        Intent.ACTION_VIEW, Uri.parse(url)
    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(context, intent, null)
}

enum class Socials(val id: Int, val url: String) {
    Git(R.drawable.git, "https://github.com/avexxx3/"),
    Insta(R.drawable.instagram, "https://www.instagram.com/armaghan.atiq"),
    LinkedIn(R.drawable.linkedin, "https://www.linkedin.com/in/armaghan-atiq/"),
}