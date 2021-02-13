package com.example.sudokusolver.util

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TableRow
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.sudokusolver.R

/**
 * Created by M Hafidh Abdul Aziz on 13/02/21.
 */

class TableEntryTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        initAttribute()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttribute()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttribute()
    }

    private fun initAttribute() {
        gravity = Gravity.CENTER
        layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f
        )
        textSize = 18f
        textAlignment = TEXT_ALIGNMENT_CENTER
        isClickable = true
        isFocusable = true
        setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    fun setEditableCell(value: Boolean) {
        isLongClickable = value
        isFocusable = value
        isClickable = value
    }

}