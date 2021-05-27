package ru.example.sed.commons.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart


@ExperimentalCoroutinesApi
@CheckResult
fun EditText.textChanges(): Flow<String> {
    return callbackFlow<String> {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable) = Unit
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                offer(s.trim().toString())
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text.trim().toString()) }
}

fun View.show(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun View.hide(hide: Boolean = true) {
    this.visibility = if (hide) View.GONE else View.VISIBLE
}

fun ViewGroup.show(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun ViewGroup.hide(hide: Boolean = true) {
    this.visibility = if (hide) View.GONE else View.VISIBLE
}

fun View.showAnimate(show: Boolean = true) {
    if (show && this.visibility == View.GONE) {
        this.alpha = 0f
        this.visibility = View.VISIBLE

        animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(null)
    } else {
        animate()
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    this@showAnimate.visibility = View.GONE
                }
            })
    }
}

fun View.enable(enable: Boolean = true) {
    this.isEnabled = enable
}

fun View.disable() {
    this.isEnabled = false
}

fun Rect.set(left: Number, top: Number, right: Number, bottom: Number) {
    this.left = left.toInt()
    this.top = top.toInt()
    this.right = right.toInt()
    this.bottom = bottom.toInt()
}

fun RectF.set(left: Number, top: Number, right: Number, bottom: Number) {
    this.left = left.toFloat()
    this.top = top.toFloat()
    this.right = right.toFloat()
    this.bottom = bottom.toFloat()
}

fun Context.getAppTypeFace(@FontRes fontId: Int): Typeface? {
    return ResourcesCompat.getFont(this, fontId)
}