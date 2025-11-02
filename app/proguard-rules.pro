# Add project specific ProGuard rules here.
-keep class com.klikcalendar.app.** { *; }
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
}
