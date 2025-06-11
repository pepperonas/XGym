# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

XGym is an Android application built with Java and the AndroidX libraries. The project uses the MVVM architecture pattern with Navigation Component for fragment-based navigation.

## Build and Development Commands

### Building the Project
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK (outputs as XGym-{versionName}.apk)
./gradlew assembleRelease

# Clean and rebuild
./gradlew clean build

# Install debug build on connected device
./gradlew installDebug
```

### Testing
```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires connected device or emulator)
./gradlew connectedAndroidTest

# Run all tests
./gradlew test connectedAndroidTest
```

### Common Development Tasks
```bash
# Generate signed APK (requires keystore configuration)
./gradlew assembleRelease

# Check for dependency updates
./gradlew dependencyUpdates

# Sync project
./gradlew sync
```

## Architecture and Structure

### Key Architectural Decisions
- **MVVM Pattern**: Each screen has a Fragment + ViewModel pair
- **Navigation**: Uses Navigation Component with bottom navigation for main app flow
- **Dependency Management**: Uses Gradle version catalogs (`gradle/libs.versions.toml`) for centralized dependency versions
- **View Binding**: Enabled for type-safe view references

### Module Structure
- Single module app (`:app`)
- Package structure: `io.celox.xgym`
  - `MainActivity`: Single activity hosting all fragments
  - `ui/`: Contains feature packages (home, dashboard, notifications)
    - Each feature has Fragment + ViewModel

### Build Configuration
- **SDK Versions**: compileSdk 35, minSdk 24, targetSdk 35
- **Java Version**: Java 11
- **APK Naming**: Configured to output as `XGym-{versionName}.apk`

### Important Files
- `app/build.gradle.kts`: Main app configuration, contains custom APK naming logic
- `gradle/libs.versions.toml`: Central dependency version management
- `app/src/main/res/navigation/mobile_navigation.xml`: Navigation graph definition

## Development Notes

### Adding New Features
When adding new screens, follow the existing pattern:
1. Create a new package under `ui/`
2. Add Fragment + ViewModel pair
3. Update `mobile_navigation.xml` and `bottom_nav_menu.xml` if it's a main navigation item
4. Follow the existing naming conventions and package structure

### Modifying APK Output
The APK naming configuration is in `app/build.gradle.kts` under the `applicationVariants` block. Current format: `XGym-{versionName}.apk`

### Resource Management
- Supports dark mode (see `values-night/themes.xml`)
- Uses Material Design components
- String resources in `res/values/strings.xml`