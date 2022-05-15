package com.task.noteapp.ui

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.task.noteapp.R
import com.task.noteapp.db.entity.Note
import com.task.noteapp.db.repo.NoteDbRepository
import com.task.noteapp.ui.base.BaseNavigator
import com.task.noteapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
class NoteViewModel(application: Application) : BaseViewModel<BaseNavigator>(application),
    KoinComponent {
    private val databaseRepo:NoteDbRepository by inject()
    var title = ObservableField<String>()
    var description = ObservableField<String>()
    var imagePath = ObservableField("")
    var readAllData:LiveData<List<Note>>?=null
    var noDataTextVisiblity=ObservableField(true)
    var context=application
    fun onClickAction(view: View?) {
        getNavigator().onClickView(view)
    }

    /*Deleting the user note using coroutines*/
    fun deleteUser(note: Note){
        try {
            viewModelScope.launch {
              databaseRepo.deleteNote(note)
            }
        } catch (e: Exception) {
            putToast(context.getString(R.string.something_went_wrong))
        }
    }
    /*Fetching all notes from DB*/
    fun getAllNotes(){
        try {
            viewModelScope.launch {
                readAllData=databaseRepo.getAllNotes()
            }
        } catch (e: Exception) {
            putToast(context.getString(R.string.something_went_wrong))
        }

    }
    /*Insert note to DB*/
    fun setNoteData(note: Note) {
        try {
            viewModelScope.launch {
                databaseRepo.addNote(note)
            }
        } catch (e: Exception) {
            putToast(context.getString(R.string.something_went_wrong))
        }

    }

    /*Updating the already created note in DB*/
    fun updateUser(note: Note) {
        try {
            viewModelScope.launch {
                databaseRepo.updateNote(note)
            }
        } catch (e: Exception) {
            putToast(context.getString(R.string.something_went_wrong))
        }

    }

}
