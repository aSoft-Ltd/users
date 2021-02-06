package tz.co.asoft

import kotlinx.serialization.Serializable

@Serializable
sealed class Principle : IPrinciple {
    @Serializable
    data class UserPrinciple(
        override val user: User,
        override val account: UserAccount,
        override val claims: Map<String, List<String>>
    ) : Principle(), IUserPrinciple

    @Serializable
    data class ClientAppPrinciple(
        override val app: ClientApp,
        override val account: UserAccount,
        override val claims: Map<String, List<String>>
    ) : Principle(), IClientAppPrinciple
}