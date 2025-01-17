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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.avex.ragraa.ui.login.Logo
import com.avex.ragraa.ui.theme.sweetie_pie

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
            "Developed by Armaghan Atiq",
            color = sweetie_pie,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            "\nits been almost a year (circa 2023) thought of making a flex clone and naming it 'Ragraa' as a haha funny; and it was, albeit for the first few times. ever since then seeing the name or associating with it has filled me with hatred for myself and now the branding is too deep for me to consider changing it now." +
                    "\n\nThis has been by far my least favorite project to work on and i can't wait for the day where i completely end up abandoning it" +
                    "\n\ni don't have alot to say about myself so this is just to fill the space up a bit more. also if you are a recruiter somewhere and are seeing this please give me an internship i can't affford to be homeless" +
                    "\n\nThe code is open source and linked below so if you have anything you'd like to add, feel free",
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
    LinkedIn(R.drawable.linkedin, "https://www.linkedin.com/in/armaghan-atiq/")
}