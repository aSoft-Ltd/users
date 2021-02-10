@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

class UsersRepo(val dao: IUsersDao) : IUsersRepo, IUsersDao by dao