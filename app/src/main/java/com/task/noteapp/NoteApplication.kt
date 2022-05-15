package com.task.noteapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.task.noteapp.di.KoinCoreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO)
        val moduleList = listOf(KoinCoreModule().viewModelModule,KoinCoreModule().roomModule)
        startKoin{
            androidContext(this@NoteApplication)
            modules(moduleList)
        }
    }
}
