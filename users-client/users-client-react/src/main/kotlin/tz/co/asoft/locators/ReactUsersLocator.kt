@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

class ReactUsersLocator(
    val dao: UsersDaoLocator,
    val repo: UsersRepoLocator,
    val viewModel: ReactUsersViewModelLocator,
    val routes: ReactUsersRoutesLocator,
) {
    constructor(dao: UsersDaoLocator) : this(
        dao = dao,
        repo = UsersRepoLocator(dao),
        viewModel = ReactUsersViewModelLocator(dao),
        routes = ReactUsersRoutesLocator(dao),
    )
}