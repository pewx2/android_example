package ru.example.sed.auth.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import ru.example.sed.auth.R
import ru.example.sed.auth.databinding.FragmentSplashBinding
import ru.example.sed.commons.ui.base.BaseFragment
import ru.example.sed.core.CredentialsNotFoundException
import ru.example.sed.core.ExpiredPasswordException

class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {
    private val splashViewModel: SplashFragmentViewModel by fragmentViewModel()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, savedState: Bundle?): View {
        binding = FragmentSplashBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        splashViewModel.checkAuthentication()
    }

    override fun invalidate() {
        withState(splashViewModel) { splashState ->
            when (val result = splashState.authorizeResult) {
                is Success -> {
                }

                is Fail -> errorHandler(result.error)
            }
        }
    }

    override fun errorHandler(throwable: Throwable) {
        when (throwable) {
            is ExpiredPasswordException -> {
                activity?.runOnUiThread { showExpiredPasswordDialog() }
            }
            is CredentialsNotFoundException -> {
                navigateToLogin()
            }
            else -> {
                super.errorHandler(throwable)
                navigateToLogin()
            }
        }
    }

    private fun showExpiredPasswordDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.expired_password_title))
            .setMessage(getString(R.string.expired_password_description, ""))
            .setPositiveButton("Понятно") { dialog, _ ->
                dialog.dismiss()
                navigateToLogin()
            }
            .setCancelable(false)
            .show()
    }

    private fun navigateToLogin() {
        findNavController().navigate(SplashFragmentDirections.toLogin())
    }
}