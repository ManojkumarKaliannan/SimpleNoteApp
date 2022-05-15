package com.task.noteapp.ui.base
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding



abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : androidx.appcompat.app.AppCompatActivity() {

    private var mViewDataBinding: T? = null
    private var mViewModel: V? = null
    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        mViewDataBinding?.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding?.executePendingBindings()
    }

    fun getViewDataBinding(): T {
        return mViewDataBinding!!
    }

    /**
     * common toast show for all screens
     *
     */
    fun putToast(message: String?) {
        Toast.makeText(applicationContext, "" + message, Toast.LENGTH_SHORT).show()
    }


}