import tz.co.asoft.*
import tz.co.asoft.ISystemPermission.Companion.global
import kotlin.test.Test

class PrincipleTest {

    private val testAccount = UserAccount(name = "Test", type = "Type", scope = null)

    @Test
    fun should_not_have_any_privilege() {
        val principle = object : IPrinciple {
            override val claims: Map<String, List<String>> = mapOf()
            override val account: UserAccount = testAccount
        }
        expect(principle.has(User.Permissions.Delete, global)).toBe(false)
    }

    @Test
    fun developer_should_pass_any_privilege_check() {
        val principle = object : IPrinciple {
            override val claims: Map<String, List<String>> = mapOf(
                "system.developer" to listOf()
            )
            override val account = testAccount
        }
        expect(principle.has(User.Permissions.Wipe, global)).toBe(true)
    }

    @Test
    fun should_pass_only_when_has_a_specific_privilege() {
        val principle = object : IPrinciple {
            override val claims: Map<String, List<String>> = mapOf(
                User.Permissions.Create.title to listOf("account(1235)")
            )
            override val account = testAccount
        }
        expect(principle.has(User.Permissions.Create, "account(1235)")).toBe(true)
        expect(principle.has(User.Permissions.Create, "account(123)")).toBe(false)
    }

    @Test
    fun should_pass_only_when_has_a_specific_privilege_from_a_pool_of_privilege() {
        val principle = object : IPrinciple {
            override val claims: Map<String, List<String>> = mapOf(
                User.Permissions.Create.title to listOf("account(1235)", "account(2345)")
            )
            override val account = testAccount
        }
        expect(principle.has(User.Permissions.Create, "account(1235)")).toBe(true)
        expect(principle.has(User.Permissions.Create, "account(123)")).toBe(false)
        expect(principle.has(User.Permissions.Create, "account(2345)")).toBe(true)
    }

    @Test
    fun should_pass_for_any_account_if_has_a_global_privilege() {
        val principle = object : IPrinciple {
            override val claims: Map<String, List<String>> = mapOf(
                User.Permissions.Create.title to listOf("global")
            )
            override val account = testAccount
        }
        expect(principle.has(User.Permissions.Create, "account(1235)")).toBe(true)
        expect(principle.has(User.Permissions.Create, "user(1235)")).toBe(true)
    }
}
