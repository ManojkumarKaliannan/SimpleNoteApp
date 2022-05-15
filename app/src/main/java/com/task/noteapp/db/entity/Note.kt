package com.task.noteapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class Note(
    val title: String,
    val description: String,
    val imagePath: String,
    val isModified:Boolean,
    val createdDateAndTime:String,
    val date:String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}