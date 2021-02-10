plugins {
    kotlin("js")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}


kotlin {
    js(IR) { library() }
    sourceSets {
        val main by getting {
            dependencies {
                api(project(":users-client-core"))
                api(asoft("viewmodel-react",vers.asoft.viewmodel))
                api(asoft("reakt-layouts", vers.asoft.reakt))
                api(asoft("reakt-tables", vers.asoft.reakt))
                api(asoft("reakt-buttons", vers.asoft.reakt))
                api(asoft("reakt-inputs", vers.asoft.reakt))
                api(asoft("reakt-text", vers.asoft.reakt))
                api(asoft("reakt-navigation", vers.asoft.reakt))
                api(asoft("reakt-media", vers.asoft.reakt))
                api(asoft("paging-react", vers.asoft.paging))
                api(asoft("form-react", vers.asoft.form))
            }
        }

        val test by getting {
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