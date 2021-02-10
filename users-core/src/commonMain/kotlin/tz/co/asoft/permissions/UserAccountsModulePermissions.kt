@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

enum class UserAccountsModulePermissions(
    override val title: String,
    override val details: String,
    override val needs: List<String> = listOf(),
) : IPermission {
    Read(
        title = "useraccounts.read",
        details = "Grants access to view/edit accounts in the system"
    ),
    Create(
        title = "useraccounts.create",
        details = "Grants access to create different accounts for the system",
        needs = listOf(Read.title)
    ),
    Update(
        title = "useraccounts.update",
        details = "Grants access to update account information",
        needs = listOf(Read.title)
    ),
    Delete(
        title = "useraccounts.delete",
        details = "Grants access to delete accounts from the system",
        needs = listOf(Read.title)
    ),
    Wipe(
        title = "useraccounts.wipe",
        details = "Grants access to permanently wipe accounts from the system",
        needs = listOf(Read.title)
    )
}