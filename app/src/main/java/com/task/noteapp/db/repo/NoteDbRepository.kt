package com.task.noteapp.db.repo

import androidx.lifecycle.LiveData
import com.task.noteapp.db.NoteDatabase
import com.task.noteapp.db.entity.Note

class NoteDbRepository(private val getNoteDao: NoteDatabase):INoteDbRepository {
    override suspend fun addNote(note: Note)= getNoteDao.getNoteDao().addNote(note)
    override suspend fun getAllNotes(): LiveData<List<Note>> =getNoteDao.getNoteDao().readAllNotes()
    override suspend fun deleteNote(note: Note)=getNoteDao.getNoteDao().deleteNote(note)
    override suspend fun updateNote(note: Note) =getNoteDao.getNoteDao().updateNote(note)

}