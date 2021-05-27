package ru.example.sed.commons.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.MavericksView
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import ru.example.sed.commons.ui.BuildConfig
import ru.example.sed.commons.ui.R
import javax.security.auth.login.LoginException

abstract class BaseFragment<T : ViewBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment(layoutId), MavericksView {
    private var _binding: T? = null
    protected var binding
        get() = _binding!!
        set(value) {
            _binding = value
        }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    override fun invalidate() {
        //default
    }

    open fun errorHandler(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Logger.e(throwable, "")
        }

        activity?.runOnUiThread {
            if (throwable is LoginException) {
                (activity as BaseActivity).logout()
                showErrorToast("Ошибка авторизации")
            } else {
                showErrorToast(throwable.message ?: "Ошибка. Попробуйте повторить")
            }
        }
    }

    open fun showErrorToast(text: String) {
        try {
            val view = requireView()
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(view.context.getColor(R.color.errorColor))
                .show()
        } catch (ex: Exception) {
            //skip
        }
    }

    open fun showSuccessToast(text: String) {
        try {
            val view = requireView()
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(view.context.getColor(R.color.successColor))
                .show()
        } catch (ex: Exception) {
            //skip
        }
    }

    open fun showInfoToast(text: String) {
        try {
            val view = requireView()
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(view.context.getColor(R.color.grey_500))
                .show()
        } catch (ex: IllegalStateException) {
            //skip
        }
    }
}
