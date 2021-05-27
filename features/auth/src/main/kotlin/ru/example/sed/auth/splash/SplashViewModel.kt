package ru.example.sed.auth.splash

import com.airbnb.mvrx.*
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.example.sed.ExampleApplication
import ru.example.sed.auth.AuthRepository
import ru.example.sed.auth.AuthorizationType

data class SplashFragmentState(
    val authorizeResult: Async<AuthorizationType> = Uninitialized
) : MavericksState

class SplashFragmentViewModel(
    initialState: SplashFragmentState = SplashFragmentState(),
    private val authRepository: AuthRepository,
) : MavericksViewModel<SplashFragmentState>(initialState) {
    fun checkAuthentication() {
        viewModelScope.launch {
            try {
                setState { SplashFragmentState(Loading()) }

                delay(1000)
                val authType = withContext(Dispatchers.IO) {
                    authRepository.authorize()
                }

                setState { SplashFragmentState(Success(authType)) }
            } catch (e: Exception) {
                Logger.e(e, "")
                setState { SplashFragmentState(Fail(e)) }
            }
        }
    }

    companion object : MavericksViewModelFactory<SplashFragmentViewModel, SplashFragmentState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: SplashFragmentState,
        ): SplashFragmentViewModel {
            val authRepository = ExampleApplication
                .appComponent(viewModelContext.activity)
                .authRepository()

            return SplashFragmentViewModel(state, authRepository)
        }
    }
}