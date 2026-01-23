# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
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

# Ensure we keep the source file and line numbers for Crashlytics
-keepattributes SourceFile, LineNumberTable, Signature, Exceptions, *Annotation*, Annotation, *JavascriptInterface*

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
#-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*,!class/unboxing/enum,code/removal/simple,code/removal/advanced,!code/removal/exception
-optimizations !field/*,!method/*,!class/*,!code/simplification/*,!code/merging,!code/removal/exception,code/removal/simple,code/removal/advanced
-flattenpackagehierarchy

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class * extends java.lang.Exception
-keep class androidx.design.** { *; }
-keep interface androidx.design.** { *; }
-keep public class androidx.design.R$* { *; }

#Remove LogCat logs
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

#support Library
-dontwarn androidx.appcompat.**
-dontwarn androidx.appcompat.**
-dontwarn androidx.design.**
-keep class androidx.appcompat.** { *; }
-keep interface androidx.appcompat.** { *; }
-keep interface androidx.annotation.** { *; }

# Test dependencies
-dontwarn org.robolectric.**
-dontwarn android.app.RobolectricActivityManager
-dontwarn org.junit.**

#
# Keeps the classes and the interfaces annotated with @Keep
#
-keep @androidx.annotation.Keep class * {
  <init>(...);
  *;
}
-keep @androidx.annotation.Keep interface * {
    *;
}

-keepclassmembers class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keep interface com.google.gson.annotations.SerializedName


-keep class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keep class kotlinx.coroutines.android.AndroidExceptionPreHandler { *; }
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.** { *; }

# Keep all generated Dagger/Hilt components and prevent obfuscation
-keepnames class * {
    @dagger.hilt.android.internal.managers.* *;
}

# Keep all Hilt-generated components and modules
-keep class dagger.hilt.** { *; }
-keep class hilt_aggregated_deps.** { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }

# Keep Hilt entry points (Hilt modules, bindings, and injected fields)
-keep class * {
    @dagger.hilt.InstallIn *;
    @dagger.hilt.EntryPoint *;
    @dagger.hilt.android.HiltAndroidApp *;
    @dagger.hilt.android.lifecycle.HiltViewModel *;
    @dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories$InternalFactoryFactory *;
}

# Keep constructors of Hilt-injected classes
-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
}

# Keep Hilt ViewModel factory methods
-keepclassmembers class * {
    @dagger.hilt.android.lifecycle.HiltViewModel *;
}

# Keep Hilt-injected activities and services
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponentManager { *; }
-keep class * extends dagger.hilt.android.HiltAndroidApp { *; }

# Keep Hilt-related metadata classes
-keep class androidx.hilt.* { *; }

-dontwarn com.sun.jna.FunctionMapper
-dontwarn com.sun.jna.JNIEnv
-dontwarn com.sun.jna.Library
-dontwarn com.sun.jna.Native
-dontwarn com.sun.jna.Platform
-dontwarn com.sun.jna.Pointer
-dontwarn com.sun.jna.Structure
-dontwarn com.sun.jna.platform.win32.Kernel32
-dontwarn com.sun.jna.platform.win32.Win32Exception
-dontwarn com.sun.jna.platform.win32.WinDef$LPVOID
-dontwarn com.sun.jna.platform.win32.WinNT$HANDLE
-dontwarn com.sun.jna.win32.StdCallLibrary
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn java.lang.instrument.ClassDefinition
-dontwarn java.lang.instrument.IllegalClassFormatException
-dontwarn java.lang.instrument.UnmodifiableClassException
-dontwarn org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher
-dontwarn org.slf4j.Logger
-dontwarn org.slf4j.LoggerFactory

# RevenueCat - Keep Google API client classes (optional dependencies)
-dontwarn com.google.api.client.http.**
-dontwarn com.google.api.client.json.**
-dontwarn com.google.api.client.util.**
-dontwarn org.joda.time.**

# RevenueCat - Keep necessary classes
-keep class com.revenuecat.purchases.** { *; }
-keep interface com.revenuecat.purchases.** { *; }

# RevenueCat - Keep Google Crypto Tink classes (used by RevenueCat)
-keep class com.google.crypto.tink.** { *; }
-dontwarn com.google.crypto.tink.**
