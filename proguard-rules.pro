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
# Add this global rule
-keepattributes Signature

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class com.yourcompany.models.** {
  *;
}
-assumenosideeffects class android.util.log{
 public static boolean isLoggable(java.lang.string,int);
 public static int v(...);
 public static int i(...);
  public static int w(...);
   public static int d(...);
    public static int e(...);
}
-ignorewarnings


-keepattributes SourceFile, LineNumberTabl
-keep class org.spongycastle.** { *; }
-dontwarn org.spongycastle.**

-keep class com.itextpdf.** { *; }

-keep class javax.xml.crypto.dsig.** { *; }
-dontwarn javax.xml.crypto.dsig.**

-keep class org.apache.jcp.xml.dsig.internal.dom.** { *; }
-dontwarn org.apache.jcp.xml.dsig.internal.dom.**

-keep class javax.xml.crypto.dom.** { *; }
-dontwarn javax.xml.crypto.dom.**

-keep class org.apache.xml.security.utils.** { *; }
-dontwarn org.apache.xml.security.utils.**

-keep class javax.xml.crypto.XMLStructure
-dontwarn javax.xml.crypto.XMLStructure


-overloadaggressively
-flattenpackagehierarchy ''
-repackageclasses ''
-adaptclassstrings
-keepattributes Annotation
-keep public class * extends javax.servlet.Servlet
-keep public class * { public protected <fields>; public protected <methods>; }

#-keepclassmembers class * extends java.io.Serializable { static final long serialVersionUID; static final java.io.ObjectStreamField[] erialPersistentFields; private void writeObject(java.io.ObjectOutputStream); private void readObject(java.io.ObjectInputStream); java.lang.Object riteReplace(); java.lang.Object readResolve();}
#-keepclasseswithmembers,allowshrinking class * { native native <methods>;}
#-assumenosideeffects public class java.lang.Throwable {public void printStackTrace();}
#-assumenosideeffects public class java.lang.Thread {public static void dumpStack();}
#-dontskipnonpubliclibraryclasses
#-dontskipnonpubliclibraryclassmembers
-dontoptimize
-dontpreverify
-keep class org.apache.log4j.** { *; }