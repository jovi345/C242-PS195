package com.app.travel.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    init {
        addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not yet implemented

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: android.text.Editable?) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    setError("Invalid email address.", null)
                } else {
                    error = null
                }
            }
        })
    }
}