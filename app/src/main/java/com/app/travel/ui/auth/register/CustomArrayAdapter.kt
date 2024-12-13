package com.app.travel.ui.auth.register

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

open class CustomArrayAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun isEnabled(position: Int): Boolean {
        // Disable the first item from Spinner
        // First item will be used for hint
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        if (position == 0) {
            // Set the hint text color gray
            view.setTextColor(context.resources.getColor(android.R.color.darker_gray))
        } else {
            view.setTextColor(context.resources.getColor(android.R.color.black))
        }
        return view
    }
}