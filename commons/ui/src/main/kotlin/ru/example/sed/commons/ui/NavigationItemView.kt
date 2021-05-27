package ru.example.sed.commons.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import ru.example.sed.commons.ui.databinding.ViewNavigationItemBinding

class NavigationItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val binding = ViewNavigationItemBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Common,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                val text = getString(R.styleable.Common_text)
                val caption = getString(R.styleable.Common_caption)
                val showTopBorder = getBoolean(R.styleable.Common_showTopBorder, true)
                val showBottomBorder = getBoolean(R.styleable.Common_showBottomBorder, true)

                binding.apply {
                    tvTitle.text = text
                    caption?.let {
                        tvCaption.show(true)
                        tvCaption.text = it
                    }

                    topBorder.show(showTopBorder)
                    bottomBorder.show(showBottomBorder)

                    when (val imageId = getResourceId(R.styleable.Common_icon, -1)) {
                        -1 -> {
                            ivIcon.hide()
                        }

                        else -> {
                            ivIcon.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    imageId
                                )
                            )
                            ivIcon.show()
                        }
                    }
                }
            } finally {
                recycle()
            }
        }
    }

    fun setCaption(caption: String) {
        binding.tvCaption.apply {
            show()
            text = caption
        }
    }
}