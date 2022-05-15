package com.task.noteapp.db.repo

import androidx.lifecycle.LiveData
import com.task.noteapp.db.entity.Note

interface INoteDbRepository {
    suspend fun addNote(note:Note)
    suspend fun getAllNotes(): LiveData<List<Note>>
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)
}