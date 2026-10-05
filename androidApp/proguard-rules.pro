# ---------- General ----------
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*, SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile

# Smaller names and flatter package structure
-repackageclasses ''
-allowaccessmodification

# Kotlin / coroutines
-dontwarn kotlinx.coroutines.debug.**
-dontwarn kotlin.reflect.jvm.internal.**

# ---------- kotlinx.serialization ----------
# The library ships its own consumer rules, which cover your @Serializable
# classes (including Supabase models). The old wildcard keeps on
# com.mashiverse.mashit.** were removed so your own code can be shrunk.
# If a release build throws a SerializationException, add a NARROW keep:
# -keep,includedescriptorclasses class com.mashiverse.mashit.<package>.<Model>$$serializer { *; }
-keepattributes RuntimeVisibleAnnotations, AnnotationDefault

# ---------- Supabase ----------
# The blanket keep on io.github.jan.supabase.** was removed.
# If Postgrest/Auth decoding fails in release, add a narrow rule, e.g.:
# -keep class io.github.jan.supabase.auth.user.** { *; }
-dontwarn io.github.jan.supabase.**

# ---------- Ktor ----------
# Ktor ships consumer rules, so the blanket keep was removed.
-dontwarn org.slf4j.**
-dontwarn java.lang.management.**

# ---------- OkHttp ----------
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ---------- Room 3 ----------
# Only the database and its generated constructor need to survive.
# Entities are accessed through generated code, not reflection.
-keep class * extends androidx.room3.RoomDatabase { <init>(); }
-keep class * implements androidx.room3.RoomDatabaseConstructor { *; }
-dontwarn androidx.room3.paging.**

# Bundled SQLite (JNI)
-keep class androidx.sqlite.driver.bundled.** { *; }
-keepclasseswithmembernames class * { native <methods>; }

# ---------- DataStore (protobuf-based internals) ----------
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}

# ---------- Koin ----------
# The ViewModel <init> keep was removed (not needed with the Koin DSL).
-dontwarn org.koin.**

# ---------- KMPNotifier / Firebase ----------
# The wildcard keep and the FirebaseMessagingService keep were removed.
# AGP generates keep rules for services declared in the manifest.
# If push breaks in release, re-add only what the crash names, e.g.:
# -keep class com.mmk.kmpnotifier.notification.<ClassName> { *; }
-dontwarn com.mmk.kmpnotifier.**

# ---------- Coil 3 ----------
-dontwarn coil3.PlatformContext

# ---------- BuildKonfig ----------
# Keys only holds constants, which are inlined at compile time. No keep needed.

# ---------- Strip logs in release ----------
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}