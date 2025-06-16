pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.google.com" ) }
        maven { url = uri("https://maven.fabric.io/public" ) }
        maven { url = uri("https://www.jitpack.io" ) }

    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.google.com" ) }
        maven { url = uri("https://maven.fabric.io/public" ) }
        maven { url = uri("https://www.jitpack.io" ) }

    }
}

rootProject.name = "TaskVine"
include(":app")