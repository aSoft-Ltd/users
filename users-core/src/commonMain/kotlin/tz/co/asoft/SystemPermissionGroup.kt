package tz.co.asoft

data class SystemPermissionGroup(
    val name: String,
    val details: String,
    val permissions: Set<ISystemPermission>
) {
    companion object {
        val DEV = SystemPermissionGroup(
            name = "System Developer",
            details = "Provide super powers to aid in developing the system",
            permissions = setOf(ISystemPermission.DEV)
        )
    }
}