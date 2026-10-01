# ---------- General ----------
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*, SourceFile, LineNumberTable
-keep class kotlin.Metadata { *; }
-renamesourcefileattribute SourceFile

# Kotlin / coroutines
-dontwarn kotlinx.coroutines.debug.**
-dontwarn kotlin.reflect.jvm.internal.**

# ---------- kotlinx.serialization (Supabase models, your @Serializable classes) ----------
-keepattributes RuntimeVisibleAnnotations, AnnotationDefault

-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.serhij.mashi.**$$serializer { *; }
-keepclassmembers class com.serhij.mashi.** {
    *** Companion;
}

# Supabase / Postgrest decode models via generic type info
-keep class io.github.jan.supabase.** { *; }
-dontwarn io.github.jan.supabase.**

# ---------- Ktor ----------
-dontwarn org.slf4j.**
-dontwarn java.lang.management.**
-dontwarn io.netty.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn reactor.blockhound.**
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { volatile <fields>; }

# ---------- OkHttp ----------
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ---------- Room 3 ----------
-keep class * extends androidx.room3.RoomDatabase { <init>(); }
-keep class * implements androidx.room3.RoomDatabaseConstructor { *; }
-keep @androidx.room3.Entity class * { *; }
-dontwarn androidx.room3.paging.**

# Bundled SQLite (JNI)
-keep class androidx.sqlite.driver.bundled.** { *; }
-keepclasseswithmembernames class * { native <methods>; }

# ---------- DataStore (protobuf-based internals) ----------
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}

# ---------- Koin ----------
-dontwarn org.koin.**
-keepclassmembers class * extends androidx.lifecycle.ViewModel { <init>(...); }

# ---------- KMPNotifier / Firebase ----------
-keep class com.mmk.kmpnotifier.** { *; }
-dontwarn com.mmk.kmpnotifier.**
-keep class * extends com.google.firebase.messaging.FirebaseMessagingService { *; }

# ---------- Coil 3 ----------
-dontwarn coil3.PlatformContext

# ---------- BuildKonfig ----------
-keep class com.serhij.mashi.Keys { *; }

# ---------- Optional: strip logs in release ----------
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
}