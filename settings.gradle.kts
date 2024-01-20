pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
/*
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven{url=uri("https:/jitpack.io")}
        maven{url=uri("https://devrepo.kakao.com/nexus/content/groups/public/")}
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { "https://devrepo.kakao.com/nexus/content/groups/public/" }
    }
}
 */

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        jcenter() // jcenter는 더 이상 사용되지 않으므로 가능하면 제거하는 것이 좋습니다.
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}
rootProject.name = "everywaffle"
include(":app")
