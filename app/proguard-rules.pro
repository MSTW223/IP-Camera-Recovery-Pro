# IP Camera Recovery Pro - Proguard Rules
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-dontwarn java.lang.invoke.**
-keep class com.ipcamerarecovery.pro.model.** { *; }
-keep class com.ipcamerarecovery.pro.discovery.** { *; }
-keep class com.ipcamerarecovery.pro.diagnostics.** { *; }
-keep class com.ipcamerarecovery.pro.recovery.** { *; }
