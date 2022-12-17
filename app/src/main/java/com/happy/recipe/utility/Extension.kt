package com.happy.recipe.utility

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.happy.recipe.R
import com.happy.recipe.utility.pagination.PageUtils
import kotlinx.coroutines.*

fun Int?.toOffset() = (this?.minus(1) ?: 0) * PageUtils.NETWORK_PAGE_SIZE

fun Int?.toPageCount() = (this ?: PageUtils.NETWORK_PAGE_SIZE).div(PageUtils.NETWORK_PAGE_SIZE)

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun View.isVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * debounce input for 1s using coroutine delay to prevent overwhelming input from user
 * and limit from spoonacular API only 1 request/s
 * action will invoked when user already input 3 characters or more
 */
fun TextInputEditText.inputListener(scope: CoroutineScope, onKeywordEvent: (String) -> Unit) {
    var debounceJob: Job? = null
    val delayTime = 1000L

    this.doAfterTextChanged { editable ->
        debounceJob?.cancel()
        debounceJob = scope.launch(Dispatchers.Main) {
            delay(delayTime)
            if ((editable?.length ?: 0) >= 3) onKeywordEvent.invoke(editable.toString())
        }
    }
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

fun TextView.setSpannedText(
    @StringRes mainText: Int,
    clickableText: String,
    onLinkClicked: () -> Unit){
    with(this) {
        val NO_FLAG = 0
        val mainString = context.getString(mainText, clickableText)
        val textSpan = ClickableLinkSpan(context.getColor(R.color.green)) {
            onLinkClicked()
        }
        val spanResult = SpannableStringBuilder(mainString).apply {
            setSpan(
                textSpan,
                mainString.indexOf(clickableText),
                clickableText.length.plus(
                    mainString.indexOf(
                        clickableText
                    )
                ),
                NO_FLAG
            )
        }
        movementMethod = LinkMovementMethod.getInstance()
        text = spanResult
    }
}