import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig") version "0.23.0"
}

val keysProperties = Properties().apply {
    val keysFile = rootProject.file("keys.properties") // or project.file("keys.properties")
    if (keysFile.exists()) {
        load(keysFile.inputStream())
    }
}

// Helper getter with fallback to environment variables (useful for CI/CD)
fun getSecret(key: String): String {
    return keysProperties.getProperty(key)
        ?: System.getenv(key)
        ?: ""
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    android {
        namespace = "com.serhij.mashi.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)

            // KTOR
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Supabase (Exposed via 'api' so Android app target can access it)
            api(libs.postgrest.kt)
            api(libs.auth.kt)
            api(libs.realtime.kt)

            // KTOR
            implementation(libs.ktor.client.core)

            // Nav
            implementation(libs.navigation.compose)

            // Icons
            implementation(libs.material.icons.extended)
        }
        iosMain.dependencies {
            // KTOR
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

buildkonfig {
    packageName = "com.serhij.mashi"
    objectName = "Keys"

    defaultConfigs {
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "MASHIT_API_KEY",
            getSecret("MASHIT_API_KEY")
        )
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}