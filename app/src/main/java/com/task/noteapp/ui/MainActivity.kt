package com.task.noteapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.task.noteapp.BR
import com.task.noteapp.R
import com.task.noteapp.adapter.BaseRecyclerViewAdapter
import com.task.noteapp.adapter.OnDataBindCallback
import com.task.noteapp.databinding.ActivityMainBinding
import com.task.noteapp.databinding.NoteItemBinding
import com.task.noteapp.db.entity.Note
import com.task.noteapp.ui.base.BaseActivity
import com.task.noteapp.ui.base.BaseNavigator
import com.task.noteapp.utills.Singleton.edit
import com.task.noteapp.utills.Singleton.noteType
import com.task.noteapp.utills.Singleton.savedNote
import org.koin.core.KoinComponent
import org.koin.core.inject


class MainActivity : BaseActivity<ActivityMainBinding,NoteViewModel>(),BaseNavigator,
    KoinComponent {

    private  val noteViewModel: NoteViewModel by inject()
    private var mAdapter: BaseRecyclerViewAdapter<Note, NoteItemBinding>? =
        null
    override fun getBindingVariable(): Int {
      return BR.noteVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): NoteViewModel {
        return noteViewModel
    }

    override fun onClickView(v: View?) {
        when(v?.id){
            R.id.fab->{
                goTo(AddUpdateActivity::class.java,null)
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        val intent = Intent(this, clazz)
        if (mExtras != null) {
            intent.putExtras(mExtras)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        noteViewModel.setNavigator(this)
        //fetching the note list
        noteViewModel.getAllNotes()
        setupAdapter()

        //observe the database data changes and setting up to the adapter
        noteViewModel.readAllData?.observe(this) { it ->
            if (it.isNotEmpty()) {
                noteViewModel.noDataTextVisibility.set(false)
                getViewDataBinding().noteListRv.adapter = mAdapter
                mAdapter?.cleatDataSet()
                mAdapter?.addDataSet( it.sortedByDescending { it.createdDateAndTime })
            } else {
                noteViewModel.noDataTextVisibility.set(true)
                mAdapter?.cleatDataSet()
            }

        }
    }

    private fun setupAdapter() {
        mAdapter = BaseRecyclerViewAdapter(
            R.layout.note_item,
            BR.noteItem, ArrayList(), object :OnDataBindCallback<NoteItemBinding>{
                override fun onItemClick(
                    view: View,
                    position: Int,
                    v: NoteItemBinding
                ) {
                    when(view){
                      v.deleteImg->{
                          //deleting the note item
                          mAdapter?.getItems()?.let {
                              noteViewModel.deleteUser(it[position])
                              mAdapter?.notifyItemChanged(position)
                          }
                      }
                        v.noteItemCl->{
                            //navigate to addUpdateActivity
                            mAdapter?.getItems()?.let {
                                savedNote=it[position]
                            }
                            val bundle=Bundle()
                            bundle.putString(noteType,edit)
                            goTo(AddUpdateActivity::class.java,bundle)
                        }
                    }
                }

                override fun onDataBind(
                    v: NoteItemBinding,
                    onClickListener: View.OnClickListener
                ) {
                    v.deleteImg.setOnClickListener(onClickListener)
                    v.noteItemCl.setOnClickListener(onClickListener)
                }
            })
    }


}