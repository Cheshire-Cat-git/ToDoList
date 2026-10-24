package com.example.todolist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.data.model.NoteEntity
import com.example.todolist.ui.viewmodels.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    navController: NavController,
    viewModel: ListViewModel,
    listId: Int
) {
    val listWithNotes by viewModel.getListWithNotesById(listId).observeAsState()
    var notesList by remember { mutableStateOf<List<NoteEntity>>(emptyList()) }
    var noteToEdit by remember { mutableStateOf<NoteEntity?>(null) }

    LaunchedEffect(listWithNotes) {
        listWithNotes?.notes?.let {
            if (notesList.isEmpty() || notesList.size != it.size) {
                notesList = it
            }
        }
    }

    val saveAndPop: () -> Unit = {
        notesList.forEach { note ->
            viewModel.updateNote(note)
        }
        navController.popBackStack()
    }

    BackHandler {
        saveAndPop()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(listWithNotes?.list?.title ?: "List Details") },
                navigationIcon = {
                    IconButton(onClick = saveAndPop) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_item/$listId") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notesList) { note ->
                    NoteItem(
                        note = note,
                        onCheckedChange = { checked ->
                            notesList = notesList.map {
                                if (it.id == note.id) it.copy(isCompleted = checked) else it
                            }
                        },
                        onEditClick = {
                            noteToEdit = note
                        },
                        onDeleteClick = {
                            viewModel.deleteNote(note)
                        }
                    )
                }
            }
        }
    }

    if (noteToEdit != null) {
        var editedTitle by remember(noteToEdit) { mutableStateOf(noteToEdit!!.title) }
        AlertDialog(
            onDismissRequest = { noteToEdit = null },
            title = { Text("Edit Item") },
            text = {
                OutlinedTextField(
                    value = editedTitle,
                    onValueChange = { editedTitle = it },
                    label = { Text("Item Title") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (editedTitle.isNotBlank()) {
                        viewModel.updateNote(noteToEdit!!.copy(title = editedTitle))
                        noteToEdit = null
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { noteToEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun NoteItem(
    note: NoteEntity,
    onCheckedChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = note.isCompleted,
                    onCheckedChange = onCheckedChange
                )
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Item",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
