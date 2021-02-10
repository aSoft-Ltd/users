@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

import react.RBuilder
import react.RProps
import tz.co.asoft.UserDetailsManager.Props
import tz.co.asoft.UserDetailsManagerViewModel.Intent
import tz.co.asoft.UserDetailsManagerViewModel.State

private class UserDetailsManager : VComponent<Props, Intent, State, UserDetailsManagerViewModel>() {
    override val viewModel by lazy { props.locator.userDetailsManager }

    class Props(val user: User, val locator: ReactUsersViewModelLocator) : RProps

    override fun componentDidMount() {
        super.componentDidMount()
        post(Intent.ViewUser(props.user))
    }

    override fun RBuilder.render(ui: State): Any = when (ui) {
        is State.Loading -> LoadingBox(ui.msg)
        is State.ShowEditableDetails -> UserDetails(
            user = ui.u,
            onChangePassword = null,
            onChangeBasicInfo = null
        )
        is State.ShowNonEditableDetails -> UserDetails(
            user = ui.u,
            onChangePassword = { post(Intent.ViewPasswordForm(ui.u)) },
            onChangeBasicInfo = { post(Intent.ViewBasicInfoForm(ui.u)) }
        )
        is State.ShowPasswordForm -> PasswordForm(
            onCancel = { post(Intent.ViewUser(ui.user)) },
            onSubmit = { oldPass, newPass ->
                post(Intent.ChangePassword(ui.user, oldPass.toByteArray(), newPass.toByteArray()))
            }
        )
        is State.ShowBasicInfoForm -> BasicInfoForm(
            user = ui.user,
            onCancel = { post(Intent.ViewUser(ui.user)) },
            onSubmit = { name, email, phone -> post(Intent.EditBasicInfo(ui.user, name, email, phone)) }
        )
        is State.Error -> ErrorBox(
            exception = ui.exception,
            actions = listOf(
                AButton.Contained("Retry", FaSync) { ui.retry() }
            )
        )
        is State.Success -> SuccessBox(ui.msg)
    }
}

fun RBuilder.UserDetailsManager(
    user: User,
    locator: ReactUsersViewModelLocator
) = child(UserDetailsManager::class.js, Props(user, locator)) {}