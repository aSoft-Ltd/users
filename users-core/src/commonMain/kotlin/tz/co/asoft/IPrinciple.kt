package tz.co.asoft

import tz.co.asoft.ISystemPermission.Companion.denied
import tz.co.asoft.ISystemPermission.Companion.global

interface IPrinciple {
    val claims: Map<String, List<String>>
    val account: UserAccount

    fun has(permission: String, value: String): Boolean {
        val sysDev = claims["system.developer"]
        if (sysDev != null) {
            return !sysDev.contains(denied)
        }
        val perm = claims[permission]
        if (perm != null && perm.contains(global)) {
            return true
        }
        return perm?.contains(value) ?: false
    }

    fun has(permission: ISystemPermission, value: String) = has(permission.title, value)
}