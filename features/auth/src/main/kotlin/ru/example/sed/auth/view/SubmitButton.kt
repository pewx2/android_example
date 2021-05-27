package ru.example.sed.auth.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ru.example.sed.auth.R
import ru.example.sed.auth.databinding.ViewSubmitButtonBinding

class SubmitButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    private val binding = ViewSubmitButtonBinding.inflate(LayoutInflater.from(context), this)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SubmitButton,
            0, 0
        ).apply {
            try {
                val text = getString(R.styleable.SubmitButton_text)
                val enabled = getBoolean(R.styleable.SubmitButton_enabled, false)

                binding.apply {
                    button.text = text
                    button.isEnabled = enabled
                }

            } finally {
                recycle()
            }
        }
    }

    fun setOnClickListener(onClick: () -> Unit) {
        binding.button.setOnClickListener { onClick() }
    }

    override fun setEnabled(enabled: Boolean) {
        binding.button.isEnabled = enabled
    }
}