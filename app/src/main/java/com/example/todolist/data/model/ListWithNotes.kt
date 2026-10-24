package com.example.todolist.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ListWithNotes(
    @Embedded val list: ListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val notes: List<NoteEntity>
)
