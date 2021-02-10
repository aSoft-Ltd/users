@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

import react.RProps

class ReactUsersRoutesLocator(
    val viewModel: ReactUsersViewModelLocator
) {
    constructor(repo: UsersRepoLocator) : this(ReactUsersViewModelLocator(repo))
    constructor(dao: UsersDaoLocator) : this(UsersRepoLocator(dao))

    object routes {
        val users = "/users"
    }

    val menus = listOf(
        NavMenu("Users", routes.users, FaUserFriends, "")
    )

    val modules: List<AbstractModuleRoute<out RProps>> = listOf(
        ModuleRoute(
            path = routes.users,
            permits = UserPermissions.values().map { it.title },
            scope = "",
            builder = { UsersManager(drawerController = null, locator = viewModel) },
        )
    )
}