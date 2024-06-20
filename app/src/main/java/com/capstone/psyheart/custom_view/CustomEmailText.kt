package com.capstone.psyheart.custom_view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.capstone.psyheart.R
import com.capstone.psyheart.utils.isValidEmail
import com.google.android.material.textfield.TextInputEditText

class CustomEmailText : TextInputEditText {
    constructor(context: Context) : super(context) {
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
        addTextChangedListener(object : TextWatcher {
            private var lastValidEmail: String? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (isValidEmail(it.toString())) {
                        this@CustomEmailText.error = null
                        lastValidEmail = it.toString()
                    } else {
                        this@CustomEmailText.error = context.getString(R.string.email_invalid)
                        if (lastValidEmail != null) {
                            this@CustomEmailText.setText(lastValidEmail)
                            this@CustomEmailText.setSelection(lastValidEmail!!.length)
                        }
                    }
                }
            }
        })
    }
}