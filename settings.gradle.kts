pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "users"
include(":users-core")

include(":users-dao-core")
project(":users-dao-core").projectDir = File("users-dao/users-dao-core")

include(":users-client-core")
project(":users-client-core").projectDir = File("users-client/users-client-core")

include(":users-client-react")
project(":users-client-react").projectDir = File("users-client/users-client-react")
