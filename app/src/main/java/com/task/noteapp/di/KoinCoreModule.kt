package com.task.noteapp.di

import android.app.Application
import androidx.room.Room
import com.task.noteapp.db.NoteDatabase
import com.task.noteapp.db.repo.NoteDbRepository
import com.task.noteapp.ui.NoteViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.dsl.module

class KoinCoreModule {

    private fun providesDatabase(application: Application):NoteDatabase =
        Room.databaseBuilder(application,NoteDatabase::class.java,"note_database")
            .build()

    val roomModule = module {
        single { providesDatabase(androidApplication()) }
        single { NoteDbRepository(get()) }
    }
    val viewModelModule = module {
         viewModel<NoteViewModel>()
    }

}