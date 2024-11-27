pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven {
            url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
        }
        maven {
            url = uri("https://devrepo.kakao.com/nexus/content/groups/public/")
        }

        mavenCentral()
        gradlePluginPortal()


    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {

        maven {
            url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
        }
        maven {
            url = uri("https://devrepo.kakao.com/nexus/content/groups/public/")
        }
        google()
        mavenCentral()

    }
}

rootProject.name = "skku_restaurant"
include(":app")
 