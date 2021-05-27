package ru.example.sed.auth.changeserver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.orhanobut.logger.Logger
import ru.example.sed.auth.R
import ru.example.sed.auth.databinding.FragmentChangeServerBinding
import ru.example.sed.commons.ui.base.BaseFragment

class ChangeServerFragment :
    BaseFragment<FragmentChangeServerBinding>(R.layout.fragment_change_server) {

    private val viewModel: ChangeServerViewModel by fragmentViewModel()

    private val viewAdapter by lazy {
        ChangeServerListAdapter {
            viewModel.setActiveServer(it.name)
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, savedState: Bundle?): View {
        binding = FragmentChangeServerBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.loadServers()
    }

    override fun invalidate() {
        withState(viewModel) {
            when (it.servers) {
                is Success -> {
                    viewAdapter.submitList(it.servers.invoke().toList())
                }

                else -> {
                }
            }
        }
    }

    private fun initViews() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.list.apply {
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    LinearLayoutManager(requireContext()).orientation
                )
            )
        }

        binding.floatingActionButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.change_server_dialog_title))
                .setView(layoutInflater.inflate(R.layout.dialog_change_server, null))
                .setPositiveButton("Добавить") { dialog, _ ->
                    val newServerField = (dialog as AlertDialog)
                        .findViewById<EditText>(R.id.new_server)

                    newServerField?.text?.let { onServerAdd(it.toString()) }
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun onServerAdd(newServer: String) {
        viewModel.addServer(newServer) {
            Logger.e(it, "")
            showErrorToast("Указан некорректный адрес сервера")
        }
    }
}