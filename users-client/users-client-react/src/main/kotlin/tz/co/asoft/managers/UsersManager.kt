@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.css.*
import react.RBuilder
import react.RProps
import styled.css
import tz.co.asoft.UsersManager.Props
import tz.co.asoft.UsersManagerViewModel.Display.*
import tz.co.asoft.UsersManagerViewModel.Intent
import tz.co.asoft.UsersManagerViewModel.State

@JsExport
class UsersManager private constructor() : VComponent<Props, Intent, State, UsersManagerViewModel>() {
    override val viewModel by lazy { props.locator.usersManager }

    class Props(
        val drawerController: MutableStateFlow<DrawerState>?,
        val locator: ReactUsersViewModelLocator
    ) : RProps

    private val inputCss: RuleSet = {
        onMobile {
            width = 90.pct
            minWidth = LinearDimension.auto
            margin(horizontal = 5.pct)
        }
    }

    private fun RBuilder.AppBar() = MainDrawerControllerConsumer { mainDrawerController ->
        NavigationAppBar(
            drawerController = props.drawerController ?: mainDrawerController,
            left = { +"Users" },
            middle = {
                SearchInput(hint = "Search Users", css = inputCss) { key ->
                    val predicate = { user: User ->
                        Json.encodeToString(User.serializer(), user)
                            .contains(key, ignoreCase = true) && !user.deleted
                    }
                    UsersManagerViewModel.post(Intent.ViewUsers(predicate = predicate))
                }
            }
        )
    }

    private fun RBuilder.ShowUsersTable(pager: Pager<User>) = Surface {
        css {
            onDesktop {
                padding(horizontal = 10.pct)
            }
        }
        PaginatedTable(
            pager = pager,
            columns = listOf(
                Column("Name") { it?.name ?: "Fullname" },
                Column("Email") { it?.emails?.firstOrNull() ?: "blank@email.com" },
                Column("Phone") { it?.phones?.firstOrNull() ?: "+xxx xxx xxx xxx" },
            )
        )
    }

    private fun RBuilder.ShowUsersGrid(pager: Pager<User>) = Surface {
        css {
            onDesktop {
                padding(horizontal = 10.pct)
            }
        }
        PaginatedGrid(pager, cols = "1fr 1fr 1fr") {
            css {
                onDesktop { gridTemplateColumns = GridTemplateColumns("1fr 1fr") }
                onMobile { gridTemplateColumns = GridTemplateColumns("1fr") }
            }
            UserView(it)
        }
    }

    override fun RBuilder.render(ui: State) {
        AppBar()
        Surface(margin = 0.5.em) {
            when (ui) {
                is State.Loading -> LoadingBox(ui.msg)
                is State.Users -> when (ui.display) {
                    is Grid -> ShowUsersGrid(ui.pager)
                    is Table -> ShowUsersTable(ui.pager)
                }
                is State.Error -> ErrorBox(
                    exception = ui.exception,
                    actions = listOf(
                        AButton.Contained("Cancel", icon = FaArrowLeft) { ui.cancel() },
                        AButton.Contained("Retry", icon = FaSync) { ui.retry() }
                    )
                )
                is State.Success -> SuccessBox(ui.msg)
            }
        }
    }
}

fun RBuilder.UsersManager(
    drawerController: MutableStateFlow<DrawerState>?,
    locator: ReactUsersViewModelLocator
) = child(UsersManager::class.js, Props(drawerController, locator)) {}