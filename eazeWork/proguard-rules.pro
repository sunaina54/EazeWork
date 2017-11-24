-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-dontwarn com.beloo.widget.**
-dontwarn rx.internal.util.**
-keepclasseswithmembernames class * {
    native <methods>;
}
