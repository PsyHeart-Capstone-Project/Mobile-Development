package com.capstone.psyheart.custom_view

import android.content.Context
import android.provider.SyncStateContract
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.capstone.psyheart.R
import com.capstone.psyheart.constants.Constants
import com.capstone.psyheart.utils.validateMinPassword
import com.google.android.material.textfield.TextInputEditText

class CustomPasswordText : TextInputEditText {
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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validatePassword(s.toString())
            }
        })
    }

    private fun validatePassword(password: String) {
        if (validateMinPassword(password)) {
            this@CustomPasswordText.error = context.getString(R.string.pwd_min8_msg)
        } else {
            this@CustomPasswordText.error = null
        }
    }
}