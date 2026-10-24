package com.example.todolist.data.repository

import androidx.lifecycle.LiveData
import com.example.todolist.data.dao.ListDao
import com.example.todolist.data.model.ListEntity
import com.example.todolist.data.model.ListWithNotes
import com.example.todolist.data.model.NoteEntity

class ListRepository(private val listDao: ListDao) {
    val allLists: LiveData<List<ListEntity>> = listDao.getAllLists()
    val allListsWithNotes: LiveData<List<ListWithNotes>> = listDao.getListsWithNotes()

    fun getListWithNotesById(listId: Int): LiveData<ListWithNotes?> {
        return listDao.getListWithNotesById(listId)
    }

    suspend fun insertList(list: ListEntity): Long {
        return listDao.insertList(list)
    }

    suspend fun insertNote(note: NoteEntity) {
        listDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteEntity) {
        listDao.updateNote(note)
    }

    suspend fun deleteList(list: ListEntity) {
        listDao.deleteList(list)
    }

    suspend fun deleteNote(note: NoteEntity) {
        listDao.deleteNote(note)
    }
}
