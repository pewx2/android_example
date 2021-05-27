package ru.example.sed.auth.changeserver

import com.airbnb.mvrx.*
import ru.example.sed.ExampleApplication
import ru.example.sed.service.CacheService
import java.net.URI


data class ChangeServerViewState(
    val servers: Async<List<ServerItem>> = Loading()
) : MavericksState

class ChangeServerViewModel(
    initialState: ChangeServerViewState = ChangeServerViewState(),
    private val cacheService: CacheService,
) : MavericksViewModel<ChangeServerViewState>(initialState) {
    fun loadServers() {
        suspend {
            val activeServer = cacheService.getActiveServer()

            cacheService.getServers()
                .map { ServerItem(it, it == activeServer) }
                .sortedBy { it.name }
        }.execute { copy(servers = it) }
    }

    fun addServer(server: String, onError: (Throwable) -> Unit) {
        try {
            URI.create(server).toURL()
            cacheService.addServer(server)
            loadServers()
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun setActiveServer(server: String) {
        cacheService.setActiveServer(server)
        loadServers()
    }

    companion object : MavericksViewModelFactory<ChangeServerViewModel, ChangeServerViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: ChangeServerViewState,
        ): ChangeServerViewModel {
            val cacheService = ExampleApplication
                .appComponent(viewModelContext.activity)
                .cacheService()

            return ChangeServerViewModel(state, cacheService)
        }
    }
}