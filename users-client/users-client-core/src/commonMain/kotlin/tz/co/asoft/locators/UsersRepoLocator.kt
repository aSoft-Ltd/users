@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

class UsersRepoLocator(
    val dao: UsersDaoLocator,
    val users: IUsersRepo = UsersRepo(dao.users),
    val userAccounts: IRepo<UserAccount> = Repo(dao.userAccounts)
)