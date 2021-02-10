package tz.co.asoft

interface IUsersDao : IDao<User> {
    fun uploadPhoto(userId: String, photo: File): Later<User>
    fun changePassword(userId: String, old: Password, new: Password): Later<User>
}