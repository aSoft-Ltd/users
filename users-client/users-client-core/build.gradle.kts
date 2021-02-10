plugins {
    kotlin("multiplatform")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    multiplatformLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":users-dao-core"))
                api(asoft("persist-client", vers.asoft.persist))
                api(asoft("krypto-core", vers.asoft.krypto))
                api(asoft("viewmodel-core", vers.asoft.viewmodel))
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