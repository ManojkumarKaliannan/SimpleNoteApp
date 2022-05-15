package com.task.noteapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.noteapp.db.dao.NoteDAO
import com.task.noteapp.db.entity.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDAO

}