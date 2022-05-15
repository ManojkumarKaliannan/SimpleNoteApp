package com.task.noteapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.task.noteapp.BR
import com.task.noteapp.R
import com.task.noteapp.databinding.ActivityAddUpdateBinding
import com.task.noteapp.db.entity.Note
import com.task.noteapp.ui.base.BaseActivity
import com.task.noteapp.ui.base.BaseNavigator
import com.task.noteapp.utills.Singleton
import com.task.noteapp.utills.Singleton.PERMISSION_CODE
import com.task.noteapp.utills.Singleton.edit
import com.task.noteapp.utills.getCurrentDate
import com.task.noteapp.utills.getCurrentDateAndTime
import org.koin.core.KoinComponent
import org.koin.core.inject


class AddUpdateActivity : BaseActivity<ActivityAddUpdateBinding, NoteViewModel>(), KoinComponent,
    BaseNavigator {
    private val noteViewModel: NoteViewModel by inject()
    private var savedNote: Note? = null
    private var noteType: String? = null
    override fun getBindingVariable(): Int {
        return BR.noteVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_update
    }

    override fun getViewModel(): NoteViewModel {
        return noteViewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel.setNavigator(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //Navigating to update and add functionality based on intent
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        noteType = intent.getStringExtra(Singleton.noteType)
        if (noteType.equals(edit)) {
            // setting up InputFields from clicked note item
            savedNote = Singleton.savedNote
            supportActionBar?.title = savedNote?.title
            getViewDataBinding().saveBtn.text = getString(R.string.update)
            setNoteData(savedNote)
        } else {
            supportActionBar?.title = getString(R.string.add_notes)
        }
    }

    private fun setNoteData(savedNote: Note?) {
        noteViewModel.title.set(savedNote?.title)
        noteViewModel.description.set(savedNote?.description)
        noteViewModel.imagePath.set(savedNote?.imagePath)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.save_btn -> {
                when {
                    noteViewModel.title.get() == null -> {
                        putToast(getString(R.string.title_empty))
                    }
                    noteViewModel.description.get() == null -> {
                        putToast(getString(R.string.description_empty))
                    }
                    else -> {
                        //checking if its edit screen
                        if (noteType.equals(edit)) {
                            //checking if user clicks update button,without doing any changes.
                            if (noteViewModel.title.get().equals(savedNote?.title) &&
                                noteViewModel.description.get().equals(savedNote?.description) &&
                                noteViewModel.imagePath.get().equals(savedNote?.imagePath)
                            ) {
                                putToast(getString(R.string.no_changes))
                            } else {
                                //updating notes to Database
                                updateNote()
                            }
                        } else {
                            //saving notes to database
                            savingNotes()
                        }

                    }
                }
            }
            R.id.picker_img -> {
                // checking whether the read run time permission enabled
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    askRuntimePermission()
                } else {
                    openGalleryForImage()
                }

            }
        }
    }

    private fun savingNotes() {
        val data = Note(
            title = noteViewModel.title.get()!!,
            description = noteViewModel.description.get()!!,
            imagePath = noteViewModel.imagePath.get()!!,
            isModified = false,
            date = getCurrentDate(System.currentTimeMillis()),
            createdDateAndTime = getCurrentDateAndTime(System.currentTimeMillis())
        )
        noteViewModel.setNoteData(data)
        onBackPressed()
    }

    private fun updateNote() {
        val data = Note(
            title = noteViewModel.title.get()!!,
            description = noteViewModel.description.get()!!,
            imagePath = noteViewModel.imagePath.get()!!,
            isModified = true,
            date = getCurrentDate(System.currentTimeMillis()),
            createdDateAndTime = getCurrentDateAndTime(System.currentTimeMillis())
        )
        data.id = savedNote!!.id
        noteViewModel.updateUser(data)
        onBackPressed()
    }

    private fun askRuntimePermission() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE)
    }

    private fun openGalleryForImage() {
        //pick image from mobile storage
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getImageContent.launch(intent)

    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {

    }

    //StartActivityForResult is deprecated
    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //saving the selected image uri
                val selectedImageUri: Uri = result.data?.data!!
                noteViewModel.imagePath.set(selectedImageUri.toString())
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGalleryForImage()
                } else {
                    putToast(getString(R.string.enable_permission))
                }
            }
        }
    }

}