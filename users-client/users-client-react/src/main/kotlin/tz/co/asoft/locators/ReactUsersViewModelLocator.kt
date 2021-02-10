@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

class ReactUsersViewModelLocator(
    val repo: UsersRepoLocator,
    val userDetailsManager: UserDetailsManagerViewModel = UserDetailsManagerViewModel(repo.users, repo.dao.user),
    val userProfilePicManager: UserProfilePicManagerViewModel = UserProfilePicManagerViewModel(repo.users, repo.dao.user),
    val usersManager: UsersManagerViewModel = UsersManagerViewModel(repo.users, repo.dao.user)
) {
    constructor(dao: UsersDaoLocator) : this(UsersRepoLocator(dao))
}