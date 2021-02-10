@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

import kotlinx.serialization.Serializable

@Serializable
data class UserAccount(
    override var uid: String? = null,
    override val name: String,
    override val owner: ResourceAccessor,
    override val claims: ResourcePermissions = ResourcePermissions(
        owner = UserAccountPermissions.values().map { it.title }.toMutableList()
    ),
    val photoUrl: String? = null,
    val type: String,
    override var deleted: Boolean = false
) : NamedEntity, Resource {
    interface Type {
        val name: String
        val details: String
        val permissionGroups: List<ModulePermission>
    }
}