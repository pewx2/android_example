package ru.example.sed.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.*
import ru.example.sed.auth.R
import ru.example.sed.auth.databinding.FragmentLoginBinding
import ru.example.sed.commons.ui.base.BaseFragment
import ru.example.sed.core.ExpiredPasswordException
import ru.example.sed.core.SedHttpException

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val loginViewModel: LoginFragmentViewModel by fragmentViewModel()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, savedState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun invalidate() {
        withState(loginViewModel) { loginState ->
            when (val result = loginState.result) {
                is Loading -> binding.form.setLoadingState()

                is Success -> {
                    binding.form.clearForm()
                }

                is Fail -> {
                    binding.form.setInputState()
                }

                else -> binding.form.setInputState()
            }
        }
    }

    override fun errorHandler(throwable: Throwable) {
        activity?.runOnUiThread {
            when (throwable) {
                is ExpiredPasswordException -> {
                    //TODO
                }

                is SedHttpException -> {
                    showErrorToast(throwable.toString())
                }

                else -> {
                    showErrorToast("Сервер недоступен. Проверьте подключение к сети")
                }
            }
        }
    }

    private fun initListeners() {
        binding.form.setup { username, password ->
            loginViewModel.login(username, password) {
                errorHandler(it)
            }
        }

        binding.form.binding.changeServer.setOnClickListener {
            navigateToChangeServer()
        }

        binding.form.binding.passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.root.smoothScrollTo(0, binding.root.bottom)
            }
        }
    }

    private fun navigateToChangeServer() {
        findNavController().navigate(LoginFragmentDirections.toChangeServer())
    }
}
