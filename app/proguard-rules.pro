# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontoptimize
-dontpreverify
-libraryjars <java.home>/lib/rt.jar
-repackageclasses
-keep public class com.google.android.gms.* { public *; }
-keepattributes LocalVariableTable, LocalVariableTypeTable
-dontwarn okio.**
-dontwarn io.sentry.**
-ignorewarnings
-dontwarn com.google.auto.value.**
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontwarn org.xmlpull.v1.**
-keep class org.xmlpull.** { *; }
-keepclassmembers class org.xmlpull.** { *; }
-dontwarn android.net.**
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-keep class com.rey.material.** { *; }
-dontwarn com.rey.material.**
-optimizations !class/unboxing/enum
-dontwarn com.google.android.gms.internal.zzhu
-keep class com.google.android.gms.internal.** { *; }
-dontwarn android.support.v4.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep public class org.jsoup.** {
      public *;
  }