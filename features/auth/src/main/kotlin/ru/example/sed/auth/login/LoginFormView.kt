package ru.example.sed.auth.login

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import ru.example.sed.auth.R
import ru.example.sed.auth.databinding.ViewLoginFormBinding
import ru.example.sed.commons.ui.*

class LoginFormView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
) : RelativeLayout(context, attr) {
    val binding = ViewLoginFormBinding.inflate(LayoutInflater.from(context), this, true)

    fun setup(onButtonPressed: (username: String, password: String) -> Unit) {
        binding.submitButton.setOnClickListener {
            val username = binding.loginInput.text?.toString() ?: ""
            val password = binding.passwordInput.text?.toString() ?: ""

            if (username.isNotBlank() && password.isNotBlank()) {
                onButtonPressed(username, password)
            }
        }

        val loginFlow = binding.loginInput.textChanges()
        val passwordFlow = binding.passwordInput.textChanges()

        loginFlow.combine(passwordFlow) { login, password ->
            val isEnabled = login.isNotBlank() && password.isNotBlank()
            binding.submitButton.isEnabled = isEnabled
        }.launchIn(CoroutineScope(Dispatchers.Main))

        binding.resetPassword.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.reset_password_dialog_title))
                .setMessage(context.getString(R.string.reset_password_dialog_message))
                .setPositiveButton("ОК") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    fun setInputState() {
        binding.apply {
            loginInputLayout.enable()
            passwordInput.enable()
            submitButton.show()
            progress.hide()
        }
    }

    fun setLoadingState() {
        binding.apply {
            loginInputLayout.disable()
            passwordInput.disable()
            submitButton.hide()
            progress.showAnimate(true)
        }
    }

    fun clearForm() {
        binding.apply {
            loginInput.text?.clear()
            passwordInput.text?.clear()
        }

        setInputState()
    }
}