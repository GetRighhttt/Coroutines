# Gradle and the Android build

This repository uses the Android Gradle plugin because the examples live in an Android project. The
examples do not require an Activity or a screen. Their top-level `main` functions make the coroutine
behavior easy to run from Android Studio, while the Android module provides the Kotlin compiler and
local test runner.

## File responsibilities

- `settings.gradle.kts` names the project and includes the `app` module.
- `build.gradle.kts` applies the Android and Kotlin plugins without owning module dependencies.
- `app/build.gradle.kts` owns the Android namespace, SDK levels, compiler target, and dependencies.
- `gradle/libs.versions.toml` keeps library and plugin versions in one place.
- `gradle/wrapper` pins the Gradle version used by `./gradlew`.

The coroutine examples use `kotlinx-coroutines-core`. Android UI code would normally add lifecycle or
ViewModel coroutine support when the repository grows a screen, but this learning repository does not
need those dependencies yet.

## Checks

Run the local tests and assemble the debug APK with:

```sh
./gradlew :app:testDebugUnitTest :app:assembleDebug
```

The tests run on the host JVM. The debug APK check confirms that the Android module, resources, and
Kotlin examples compile together. Generated files belong in `build/` and should not be added to Git.
