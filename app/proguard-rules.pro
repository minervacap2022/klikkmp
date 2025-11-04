# Add project specific ProGuard rules here.
-keep class com.klikcalendar.shared.** { *; }
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
}
