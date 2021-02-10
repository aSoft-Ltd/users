@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

enum class UserPermissions(
    override val title: String,
    override val details: String,
    override val needs: List<String> = listOf(),
) : IPermission {
    Read(
        title = "read",
        details = "Grants access to view this user"
    ),
    Update(
        title = "update",
        details = "Grants access to update this user's information",
        needs = listOf(Read.title)
    ),
    Delete(
        title = "delete",
        details = "Grants access to delete this users from the system",
        needs = listOf(Read.title)
    ),
    Wipe(
        title = "wipe",
        details = "Grants access to permanently wipe this users from the system",
        needs = listOf(Read.title)
    )
}