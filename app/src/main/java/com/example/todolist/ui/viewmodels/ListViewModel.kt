package com.example.todolist.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.model.ListEntity
import com.example.todolist.data.model.ListWithNotes
import com.example.todolist.data.model.NoteEntity
import com.example.todolist.data.repository.ListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ListRepository
    val allLists: LiveData<List<ListEntity>>
    val allListsWithNotes: LiveData<List<ListWithNotes>>

    init {
        val listDao = AppDatabase.getDatabase(application).listDao()
        repository = ListRepository(listDao)
        allLists = repository.allLists
        allListsWithNotes = repository.allListsWithNotes
    }

    fun getListWithNotesById(listId: Int): LiveData<ListWithNotes?> {
        return repository.getListWithNotesById(listId)
    }

    fun insertList(list: ListEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertList(list)
    }

    fun insertListAndGetId(list: ListEntity, onInserted: (Long) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val id = repository.insertList(list)
        withContext(Dispatchers.Main) {
            onInserted(id)
        }
    }

    fun insertNote(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertNote(note)
    }

    fun updateNote(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(note)
    }

    fun deleteList(list: ListEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteList(list)
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(note)
    }
}
