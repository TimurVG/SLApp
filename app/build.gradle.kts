android {
    namespace 'com.example.slapp'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.slapp"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }
    // ... остальное без изменений
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}