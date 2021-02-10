@file:Suppress("PackageDirectoryMismatch")

package tz.co.asoft

fun User.ref() = UserRef(
    uid = uid ?: throw Exception("Attempting to create a reference of a User(uid=null)"),
    name = name,
    photoUrl = photoUrl
)

fun User.asResourceAccessor() = ResourceAccessor(uid ?: "", name, photoUrl)