package com.happy.recipe.utility

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class ClickableLinkSpan(val color: Int, val onClick: () -> Unit) : ClickableSpan() {
    override fun onClick(widget: View) {
        onClick()
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.color = color
        ds.isUnderlineText = true
    }
}