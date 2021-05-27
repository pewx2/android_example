import dependencies.Dependencies
import extensions.buildConfigStringField
import extensions.getLocalProperty

plugins {
    id("commons.android-library")
}

android {
    buildFeatures.viewBinding = true

    buildTypes.forEach {
        try {
            it.buildConfigStringField(
                "SED_API_SERVER",
                System.getenv("SED_API_SERVER") ?: getLocalProperty("sed.api.server")
            )
        } catch (ignored: Exception) {
            throw InvalidUserCodeException("You should define `sed.host` in local.properties")
        }
    }
}

dependencies {
    implementation(Dependencies.KOTLIN)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.CONSTRAINT_LAYOUT)
    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.MVRX)
}
