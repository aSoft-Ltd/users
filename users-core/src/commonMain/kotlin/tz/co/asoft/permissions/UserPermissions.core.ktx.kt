@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

fun User.can(action: UsersModulePermissions) = can(action as IPermission)

private fun User.can(action: IPermission): Boolean {
    val permissions = modulePermissions.flatMap { it.claims }
    if (permissions.contains(ModulePermission.DEV.title) || permissions.contains(action.title)) {
        return true
    }
    return false
}

fun User.can(action: IPermission, onResource: Resource): Boolean {
    if (can(action)) return true
    return asResourceAccessor().can(action, onResource)
}