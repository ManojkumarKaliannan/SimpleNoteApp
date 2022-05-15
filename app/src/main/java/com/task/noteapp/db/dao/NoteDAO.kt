package com.task.noteapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.task.noteapp.db.entity.Note

@Dao
interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // if there is same user,then we will ignore it
    suspend fun addNote(note: Note) // suspend is added as we will use coroutine

    @Update
    suspend fun updateNote(note: Note)

    //deletes single user
    @Delete
    suspend fun deleteNote(note: Note)

    //custom query to delete all user
    @Query("DELETE FROM user_info")
    fun deleteAllUsers()

    @Query("SELECT * FROM user_info")
    fun readAllNotes(): LiveData<List<Note>>
}