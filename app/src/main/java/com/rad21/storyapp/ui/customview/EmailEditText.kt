package com.rad21.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.rad21.storyapp.R

class EmailEditText : AppCompatEditText {

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

    private fun init() {

        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, end: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, count: Int, end: Int) {
                if (s.isNotEmpty() && !isValidEmail(s.toString())) error = context.getString(R.string.email_error)
            }

            override fun afterTextChanged(s: Editable) {}

        })

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Email"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun isValidEmail(email: String): Boolean {
      return email.contains(context.getString(R.string.email_error))
    }


}