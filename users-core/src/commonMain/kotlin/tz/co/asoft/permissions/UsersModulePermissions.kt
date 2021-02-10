@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

enum class UsersModulePermissions(
    override val title: String,
    override val details: String,
    override val needs: List<String> = listOf(),
) : IPermission {
    ReadUsers(
        title = "users.read",
        details = "Grants access to view/edit users in the system"
    ),
    Create(
        title = "users.create",
        details = "Grants access to create different users for the system",
        needs = listOf(ReadUsers.title)
    ),
    Update(
        title = "users.update",
        details = "Grants access to update user information",
        needs = listOf(ReadUsers.title)
    ),
    Delete(
        title = "users.delete",
        details = "Grants access to delete users from the system",
        needs = listOf(ReadUsers.title)
    ),
    Wipe(
        title = "users.wipe",
        details = "Grants access to permanently wipe users from the system",
        needs = listOf(ReadUsers.title)
    )
}