@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

enum class UserAccountPermissions(
    override val title: String,
    override val details: String,
    override val needs: List<String> = listOf(),
) : IPermission {
    Read(
        title = "read",
        details = "Grants access to view this user account"
    ),
    Update(
        title = "update",
        details = "Grants access to update this user account's information",
        needs = listOf(Read.title)
    ),
    Delete(
        title = "delete",
        details = "Grants access to delete this user account from the system",
        needs = listOf(Read.title)
    ),
    Wipe(
        title = "wipe",
        details = "Grants access to permanently wipe this user account from the system",
        needs = listOf(Read.title)
    )
}