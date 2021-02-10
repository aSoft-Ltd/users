package tz.co.asoft

class UsersDaoLocator(
    val user: User,
    val users: IUsersDao,
    val userAccounts: IDao<UserAccount>
)