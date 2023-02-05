package com.f0x1d.foxbin.ui.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.f0x1d.foxbin.R
import com.f0x1d.foxbin.database.entity.FoxBinNote
import com.f0x1d.foxbin.extensions.shareNote
import com.f0x1d.foxbin.model.Screen
import com.f0x1d.foxbin.viewmodel.NotesViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.NoteItem(note: FoxBinNote, accessToken: String?, navController: NavController, viewModel: NotesViewModel) {
    val context = LocalContext.current

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateItemPlacement(),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondaryContainer),
        onClick = { navController.navigate("${Screen.Note.route}/${note.slug}") }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp)
            ) {
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = note.slug,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.size(10.dp))

                Text(text = Date(note.date).toLocaleString())
                Spacer(modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.size(10.dp))

            Column(modifier = Modifier.padding(end = 5.dp)) {
                Spacer(modifier = Modifier.size(5.dp))
                IconButton(onClick = { context.shareNote(note.slug) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                if (accessToken != null) {
                    IconButton(onClick = { viewModel.delete(note.slug) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.size(5.dp))
            }
        }
    }
}