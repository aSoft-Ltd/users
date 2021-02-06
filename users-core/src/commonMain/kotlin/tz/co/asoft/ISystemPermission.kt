package tz.co.asoft

interface ISystemPermission {
    val title: String
    val details: String
    val needs: List<String>

    companion object {
        const val global = "global"
        const val denied = "denied"

        val DEV: ISystemPermission = SystemPermission(
            name = "system.developer",
            details = "Grant's permissions to every action on the system (Should only be used in development)"
        )
    }
}
