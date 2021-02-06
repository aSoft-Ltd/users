package tz.co.asoft

fun SystemPermission(
    name: String,
    details: String,
    needs: List<String> = listOf()
): ISystemPermission = object : ISystemPermission {
    override val title = name
    override val details = details
    override val needs: List<String> = needs

    override fun equals(other: Any?) = when (other) {
        is ISystemPermission -> title == other.title
        else -> false
    }

    override fun hashCode(): Int = title.hashCode()

    override fun toString(): String = "SystemPermission(title=$name)"
}