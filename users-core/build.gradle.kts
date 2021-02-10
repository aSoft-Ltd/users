plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}


kotlin {
    multiplatformLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlinx("datetime", vers.kotlinx.datetime))
                api(asoft("persist-core", vers.asoft.persist))
                api(asoft("phone-core", vers.asoft.contacts))
                api(asoft("email-core", vers.asoft.contacts))
                api(asoft("permissions-resources", vers.asoft.permission))
                api(asoft("permissions-modules", vers.asoft.permission))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(asoft("test-core", vers.asoft.test))
                implementation(asoft("expect-core", vers.asoft.expect))
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.users,
    description = "A Kotlin Multiplatform Library with entities required for access to different resources"
)