@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

import kotlinx.css.*
import org.w3c.files.File
import react.RBuilder
import react.RProps
import styled.css
import styled.styledDiv
import kotlin.Float
import tz.co.asoft.UserProfilePicManager.Props
import tz.co.asoft.UserProfilePicManagerViewModel.Intent
import tz.co.asoft.UserProfilePicManagerViewModel.State

@JsExport
class UserProfilePicManager private constructor() : VComponent<Props, Intent, State, UserProfilePicManagerViewModel>() {

    override val viewModel by lazy { props.locator.userProfilePicManager }

    class Props(
        val u: User,
        val aspectRatio: Float,
        val textFontSize: LinearDimension,
        val radius: LinearDimension,
        val locator: ReactUsersViewModelLocator
    ) : RProps

    override fun componentDidMount() {
        super.componentDidMount()
        post(Intent.ViewPicture(props.u))
    }

    override fun RBuilder.render(ui: State) = styledDiv {
        css { cursor = Cursor.pointer }
        when (ui) {
            is State.Loading -> LoadingBox(ui.msg)
            is State.Success -> SuccessBox(ui.msg)
            is State.EditPhoto -> ImageEditor(
                file = ui.image,
                aspectRatio = props.aspectRatio,
                textFontSize = props.textFontSize,
                onCancel = { post(Intent.ViewPicture(props.u)) },
                onSubmit = { post(Intent.UploadPhoto(props.u, it)) }
            )
            is State.ShowEditablePicture -> ProfilePic(
                name = ui.u.name,
                src = ui.u.photoUrl,
                textFontSize = props.textFontSize,
                aspectRatio = props.aspectRatio,
                radius = props.radius,
                onEdit = { file: File -> post(Intent.EditPhoto(file)) }
            )
            is State.ShowNonEditablePicture -> ProfilePic(
                name = ui.u.name,
                src = ui.u.photoUrl,
                textFontSize = props.textFontSize,
                aspectRatio = props.aspectRatio,
                radius = props.radius,
                onEdit = null
            )
            is State.Error -> ErrorBox(
                exception = ui.exception,
                actions = listOf(
                    AButton.Contained("Retry", FaSync) { ui.retry() },
                    AButton.Contained("Cancel", FaTimes) { ui.cancel() }
                )
            )
        }
    }
}

fun RBuilder.UserProfilePicManager(
    user: User,
    aspectRatio: Float = 1f / 1f,
    radius: LinearDimension = 10.px,
    textFontSize: LinearDimension = 3.rem,
    locator: ReactUsersViewModelLocator
) = child(UserProfilePicManager::class.js, Props(user, aspectRatio, textFontSize, radius, locator)) {}
