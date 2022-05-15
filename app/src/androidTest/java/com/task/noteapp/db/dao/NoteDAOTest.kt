package com.task.noteapp.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.task.noteapp.db.NoteDatabase
import com.task.noteapp.db.entity.Note
import com.task.noteapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit Tests for Database
 *
 * Unit tests run on local JVM
 * whereas,
 * Instrumentation tests run on physical/real device or emulator
 *
 * Performing unit tests for db in instrumented tests set as SQLite version for devices and JVM varies.
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class NoteDAOTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NoteDatabase
    private lateinit var databaseDao: NoteDAO

    @Before
    fun initDb() {
        //Instantiating temporary database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()

        //Getting reference to Dao
        databaseDao = database.getNoteDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun db_test() = runBlockingTest {
        //GIVEN - Note data
        val note = getNote(title = "Title", modified = false)

        //Inserting into DB
        databaseDao.addNote(note)

        //WHEN - Getting Note data from database
        val result: List<Note>? = databaseDao.readAllNotes().getOrAwaitValue()

        //THEN - Asserting data
        assertThat(result, `is`(notNullValue()))
        assertThat(
            result?.size, Is.`is`(1)
        )
    }

    @Test
    fun addNote_oneNote_returnsCompleteList() = runBlockingTest {
        //GIVEN - Note data
        val note = getNote(title = "Hello", modified = false)

        //Inserting into DB
        databaseDao.addNote(note)

        //WHEN - Getting Note data from database
        val result: List<Note>? = databaseDao.readAllNotes().getOrAwaitValue()

        //THEN - Asserting data
        assertThat(result, `is`(notNullValue()))
        assertThat(
            result?.get(0)?.title, `is`("Hello")
        )
        assertThat(
            result?.get(0)?.date, `is`("1990")
        )
        assertThat(
            result?.get(0)?.isModified, `is`(false)
        )
    }

    @Test
    fun updateNote_oneNote_returnsSuccess() = runBlockingTest {
        //GIVEN - Note data
        val note = getNote(title = "New Note", modified = false)

        //Inserting into DB
        databaseDao.updateNote(note)

        //WHEN - Getting Note data from database
        val result: List<Note>? = databaseDao.readAllNotes().getOrAwaitValue()

        //THEN - Asserting data
        assertThat(result, `is`(notNullValue()))
        assertThat(
            result?.size,
            Is.`is`(0) //Expecting ZERO since trying to update Note without inserting it first
        )
    }

    @Test
    fun deleteAllNote_returnsEmptyList() = runBlockingTest {
        //GIVEN - Note data
        val note = getNote(title = "Title", modified = true)

        //Inserting into DB
        databaseDao.addNote(note)

        //WHEN - Getting Note data from database
        val intermediateResult: List<Note>? = databaseDao.readAllNotes().getOrAwaitValue()
        assertThat(intermediateResult, `is`(notNullValue()))
        assertThat(
            intermediateResult?.size, Is.`is`(1)
        )

        //Performing delete
        databaseDao.deleteAllUsers()
        val deletionResult: List<Note>? = databaseDao.readAllNotes().getOrAwaitValue()

        //THEN - Asserting data
        assertThat(deletionResult, `is`(emptyList()))
        assertThat(
            deletionResult?.size, Is.`is`(0)
        )
    }


    private fun getNote(title: String, modified: Boolean): Note {
        return Note(
            title = title,
            description = "Lorem ipsum dolor sit amet...",
            imagePath = "www.demo.com",
            isModified = modified,
            createdDateAndTime = "01/01/1990",
            date = "1990"
        )
    }

}