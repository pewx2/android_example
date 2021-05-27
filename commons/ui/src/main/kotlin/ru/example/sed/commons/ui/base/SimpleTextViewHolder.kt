package ru.example.sed.commons.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.example.sed.commons.ui.databinding.ViewSimpleTextBinding

class SimpleTextViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder<ViewSimpleTextBinding>(
    binding = ViewSimpleTextBinding.inflate(inflater, parent, false)
) {
    fun bind(text: String) {
        binding.text.text = text
    }
}