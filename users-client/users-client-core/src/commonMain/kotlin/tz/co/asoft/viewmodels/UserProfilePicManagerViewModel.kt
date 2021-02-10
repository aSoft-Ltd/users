@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tz.co.asoft.UserProfilePicManagerViewModel.Intent
import tz.co.asoft.UserProfilePicManagerViewModel.State

class UserProfilePicManagerViewModel(
    private val repo: IUsersRepo,
    private val activeUser: User,
) : VModel<Intent, State>(State.Loading("Loading User")) {
    companion object : IntentBus<Intent>()
    sealed class State {
        data class Loading(val msg: String) : State()
        data class ShowEditablePicture(val u: User) : State()
        data class ShowNonEditablePicture(val u: User) : State()
        data class EditPhoto(val image: File) : State()
        data class Error(val exception: Throwable, val forUser: User, val origin: Intent) : State() {
            val retry = { post(origin) }
            val cancel = { post(Intent.ViewPicture(forUser)) }
        }

        data class Success(val msg: String) : State()
    }

    sealed class Intent {
        data class ViewPicture(val user: User) : Intent()
        data class UploadPhoto(val user: User, val photo: File) : Intent()
        data class EditPhoto(val file: File) : Intent()
    }

    init {
        observeIntentBus()
    }

    override fun CoroutineScope.execute(i: Intent): Any = when (i) {
        is Intent.UploadPhoto -> uploadPhoto(i)
        is Intent.EditPhoto -> ui.value = State.EditPhoto(i.file)
        is Intent.ViewPicture -> viewPicture(i)
    }

    private fun viewPicture(i: Intent.ViewPicture) {
        ui.value = if (activeUser.uid == i.user.uid) {
            State.ShowEditablePicture(i.user)
        } else {
            State.ShowNonEditablePicture(i.user)
        }
    }

    private fun CoroutineScope.uploadPhoto(i: Intent.UploadPhoto) = launch {
        flow {
            require(activeUser.can(UserPermissions.Update, i.user)) {
                "You do not have the permission to change ${i.user.name}'s profile photo"
            }
            emit(State.Loading("Uploading photo"))
            val userId = i.user.uid ?: throw Exception("Can't update profile picture for a User(uid=null)")
            repo.uploadPhoto(userId, i.photo).await()
            emit(State.Success("Profile photo successfully changed"))
        }.catch {
            emit(State.Error(it, i.user, i))
        }.collect {
            ui.value = it
        }
    }
}