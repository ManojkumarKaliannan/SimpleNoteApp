package com.task.noteapp.utills

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.task.noteapp.R
import com.task.noteapp.db.entity.Note

object Singleton {
    var savedNote:Note?=null
    const val noteType="noteType"
    const val edit="Edit"
    const val PERMISSION_CODE = 1001

    @BindingAdapter(value=["customImagePath", "placeholder"], requireAll=false)
    @JvmStatic
    fun imageUrls(view: ImageView, customImagePath: String?, placeholder:Drawable) {
        if (customImagePath != null)
            Glide.with(view.context)
                .load(customImagePath)
                .placeholder(placeholder)
                .into(view)
    }
}