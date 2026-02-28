# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\coren\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# --- Gson Configuration ---

# Empêche ProGuard de supprimer les annotations de Gson
-keepattributes Signature
-keepattributes *Annotation*

# Protège les modèles de données sérialisés en JSON
# Cela garantit que les noms de champs ne seront pas renommés
-keep class fr.corentin.roux.x_wing_score_tracker.model.** { *; }

# --- Room Configuration ---

# Les règles de Room sont normalement gérées automatiquement,
# mais on garde ces attributs par sécurité
-keepattributes ElementPrecision, *Annotation*, EnclosingMethod

# Si vous utilisez des classes spécifiques pour Gson qui ne sont pas dans le package model,
# ajoutez-les ici.
