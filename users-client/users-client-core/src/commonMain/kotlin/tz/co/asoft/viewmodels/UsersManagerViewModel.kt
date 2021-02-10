@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tz.co.asoft.UsersManagerViewModel.Intent
import tz.co.asoft.UsersManagerViewModel.State
import tz.co.asoft.UsersModulePermissions.ReadUsers

class UsersManagerViewModel(
    private val repo: IRepo<User>,
    private val activeUser: User,
) : VModel<Intent, State>(State.Loading("Loading")) {
    companion object : IntentBus<Intent>() {
        var USERS_PER_PAGE = 10
    }

    private val pagingSource = GenericPagingSource(repo)
    private var pager = pagingSource.pager(USERS_PER_PAGE)

    init {
        observeIntentBus()
        post(Intent.ViewUsers(USERS_PER_PAGE, null))
    }

    sealed class Display {
        companion object {
            val default: Display = Grid(cols = 2)
            var current = default
        }

        class Grid(val cols: Int) : Display()
        object Table : Display()
    }

    sealed class State {
        data class Loading(val msg: String) : State()
        data class Users(val pager: Pager<User>, val display: Display) : State()
        data class Error(val exception: Throwable, val origin: Intent) : State() {
            val cancel = { post(Intent.ViewUsers(predicate = null)) }
            val retry = { post(origin) }
        }

        data class Success(val msg: String) : State()
    }

    sealed class Intent {
        data class ViewUsers(val usersPerPage: Int = USERS_PER_PAGE, val predicate: ((User) -> Boolean)?) : Intent()
    }

    override fun CoroutineScope.execute(i: Intent): Any = when (i) {
        is Intent.ViewUsers -> viewUsers(i)
    }

    private fun CoroutineScope.viewUsers(intent: Intent.ViewUsers) = launch {
        flow<State> {
            require(activeUser.can(ReadUsers)) { "You are not authorized to view all users" }
            emit(State.Users(pager, Display.current))
        }.catch {
            emit(State.Error(Exception("Failed to display all users for you", it), intent))
        }.collect { ui.value = it }
    }
}