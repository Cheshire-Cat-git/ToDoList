package com.example.todolist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.todolist.data.model.ListEntity
import com.example.todolist.data.model.ListWithNotes
import com.example.todolist.data.model.NoteEntity

@Dao
interface ListDao {
    @Query("SELECT * FROM lists ORDER BY timestamp DESC")
    fun getAllLists(): LiveData<List<ListEntity>>

    @Transaction
    @Query("SELECT * FROM lists ORDER BY timestamp DESC")
    fun getListsWithNotes(): LiveData<List<ListWithNotes>>

    @Transaction
    @Query("SELECT * FROM lists WHERE id = :listId")
    fun getListWithNotesById(listId: Int): LiveData<ListWithNotes?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: NoteEntity)

    @Transaction
    @Delete
    suspend fun deleteList(list: ListEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)
}
