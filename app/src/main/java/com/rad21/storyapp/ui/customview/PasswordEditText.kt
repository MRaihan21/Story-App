package com.rad21.storyapp.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.rad21.storyapp.R

class PasswordEditText: AppCompatEditText {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {

        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (s.toString().length < 8) {
                    setError(context.getString(R.string.password_error), null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) { }

        })

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}