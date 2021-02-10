@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import tz.co.asoft.UserDetailsManagerViewModel.Intent
import tz.co.asoft.UserDetailsManagerViewModel.State
import tz.co.asoft.UserPermissions.Update

class UserDetailsManagerViewModel(
    private val repo: IUsersRepo,
    private val activeUser: User
) : VModel<Intent, State>(State.Loading("Loading")) {
    companion object : IntentBus<Intent>()
    sealed class State {
        data class Loading(val msg: String) : State()
        data class ShowEditableDetails(val u: User) : State()
        data class ShowNonEditableDetails(val u: User) : State()
        data class ShowPasswordForm(val user: User) : State()
        data class ShowBasicInfoForm(val user: User) : State()
        data class Error(val exception: Throwable, val origin: Intent) : State() {
            val retry = { post(origin) }
        }

        data class Success(val msg: String) : State()
    }

    sealed class Intent {
        data class ViewPasswordForm(val user: User) : Intent()
        data class ViewBasicInfoForm(val u: User) : Intent()
        data class ViewUser(val user: User) : Intent()
        data class ChangePassword(val forUser: User, val old: ByteArray, val new: ByteArray) : Intent()
        data class EditBasicInfo(val forUser: User, val name: String, val email: String, val phone: String) : Intent()
    }

    override fun CoroutineScope.execute(i: Intent): Any = when (i) {
        is Intent.ViewPasswordForm -> ui.value = State.ShowPasswordForm(i.user)
        is Intent.ViewBasicInfoForm -> ui.value = State.ShowBasicInfoForm(i.u)
        is Intent.ViewUser -> viewUser(i)
        is Intent.ChangePassword -> changePassword(i)
        is Intent.EditBasicInfo -> editBasicInfo(i)
    }

    private fun CoroutineScope.editBasicInfo(i: Intent.EditBasicInfo) = launch {
        flow {
            require(activeUser.can(Update, i.forUser)) {
                "You do not have the permissions to edit ${i.forUser.name}'s basic info"
            }
            emit(State.Loading("Editing information"))
            val userToEdit = i.forUser.copy(
                name = i.name,
                emails = (i.forUser.emails.toSet() + i.email).toList(),
                phones = (i.forUser.phones.toSet() + i.phone).toList()
            )
            val user = repo.edit(userToEdit).await()
            emit(State.Success("Successfully changed ${user.name}'s information"))
        }.catch {
            emit(State.Error(it, i))
        }.collect {
            ui.value = it
        }
    }

    private fun CoroutineScope.changePassword(i: Intent.ChangePassword) = launch {
        flow {
            require(activeUser.can(Update, i.forUser)) {
                "You do not have the permissions to change ${i.forUser.name}'s password"
            }
            emit(State.Loading("Changing your password"))
            val userId = i.forUser.uid ?: throw Exception("Can't change password for User(uid=null)")
            repo.changePassword(userId, SHA256.digest(i.old).hex, SHA256.digest(i.new).hex).await()
            emit(State.Success("Password changed successfully"))
        }.catch {
            emit(State.Error(it, i))
        }.collect {
            ui.value = it
        }
    }

    private fun viewUser(i: Intent.ViewUser) {
        ui.value = if (activeUser.can(Update, i.user)) {
            State.ShowEditableDetails(u = i.user)
        } else {
            State.ShowNonEditableDetails(u = i.user)
        }
    }
}