package ru.example.sed.auth.login

import com.airbnb.mvrx.*
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.example.sed.ExampleApplication
import ru.example.sed.auth.AuthRepository

data class LoginFragmentState(
    val result: Async<Unit> = Uninitialized
) : MavericksState

class LoginFragmentViewModel(
    initState: LoginFragmentState = LoginFragmentState(),
    private val authRepository: AuthRepository,
) : MavericksViewModel<LoginFragmentState>(initState) {
    fun login(login: String, password: String, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                setState { copy(result = Loading()) }

                withContext(Dispatchers.IO) {
                    authRepository.signIn(login, password)
                }

                setState { copy(result = Success(Unit)) }
            } catch (e: Exception) {
                onError(e)
                setState { copy(result = Fail(e)) }
            }
        }
    }

    companion object : MavericksViewModelFactory<LoginFragmentViewModel, LoginFragmentState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: LoginFragmentState,
        ): LoginFragmentViewModel {
            val authRepository = ExampleApplication
                .appComponent(viewModelContext.activity)
                .authRepository()

            return LoginFragmentViewModel(state, authRepository)
        }
    }
}