package ru.example.sed.auth.changeserver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.sed.auth.databinding.ViewServerItemBinding
import ru.example.sed.commons.ui.base.BaseListAdapter
import ru.example.sed.commons.ui.base.BaseViewHolder
import ru.example.sed.commons.ui.show

class ChangeServerListAdapter(val onItemClick: (ServerItem) -> Unit) : BaseListAdapter<ServerItem>(
    itemsSame = { old, new -> old.name == new.name },
    contentsSame = { old, new -> old.name == new.name && old.isSelected == new.isSelected }
) {
    class ChangeServerViewHolder(
        inflater: LayoutInflater
    ) : BaseViewHolder<ViewServerItemBinding>(
        binding = ViewServerItemBinding.inflate(inflater)
    ) {
        fun bind(item: ServerItem) {
            binding.serverName.text = item.name
            binding.serverSelected.show(item.isSelected)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ) = ChangeServerViewHolder(inflater)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChangeServerViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
                holder.itemView.setOnClickListener { onItemClick(item) }
            }
        }
    }
}