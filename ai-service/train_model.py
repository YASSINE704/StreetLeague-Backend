"""
=============================================================================
SCRIPT D'ENTRAÎNEMENT DU MODÈLE DE RECOMMANDATION D'EXERCICES
=============================================================================
Ce script :
1. Charge le dataset de 120 exercices (CSV)
2. Crée des features textuelles et numériques
3. Entraîne un modèle KNN (K-Nearest Neighbors)
4. Exporte le modèle pour que Flask (app.py) puisse le charger

ALGORITHME : Content-Based Filtering
- TF-IDF pour vectoriser le texte (type, objectif, muscles, etc.)
- MinMaxScaler pour normaliser les features numériques
- KNN avec similarité cosinus pour trouver les exercices similaires

EXÉCUTER CE SCRIPT AVANT de lancer app.py :
    python train_model.py
=============================================================================
"""

# ============================================================
# IMPORTS - Bibliothèques nécessaires
# ============================================================
import pandas as pd          # Pour charger et manipuler le CSV (DataFrames)
import numpy as np           # Pour les calculs numériques (arrays)
from sklearn.feature_extraction.text import TfidfVectorizer  # Transforme du texte en vecteurs numériques
from sklearn.preprocessing import LabelEncoder, MinMaxScaler  # LabelEncoder: catégories→nombres, MinMaxScaler: normalise entre 0 et 1
from sklearn.neighbors import NearestNeighbors  # Algorithme KNN : trouve les K voisins les plus proches
from scipy.sparse import hstack, csr_matrix     # Pour combiner des matrices creuses (TF-IDF est creux)
import joblib                # Pour sauvegarder/charger le modèle entraîné (.joblib)
import os                    # Pour gérer les chemins de fichiers

# ============================================================
# ÉTAPE 1 : Se placer dans le bon dossier
# ============================================================
script_dir = os.path.dirname(os.path.abspath(__file__))  # Dossier où se trouve ce script
os.chdir(script_dir)  # Se déplacer dans ce dossier (pour que les chemins relatifs marchent)

# ============================================================
# ÉTAPE 2 : Charger le dataset des exercices
# ============================================================
# Le fichier CSV contient 120 exercices avec : nom, type, difficulté, durée,
# équipement, objectif, muscles ciblés, intensité, calories, etc.
df = pd.read_csv('data/exercises_dataset.csv')
print(f'Dataset loaded: {df.shape[0]} exercises, {df.shape[1]} columns')
# Résultat : "Dataset loaded: 120 exercises, 12 columns"

# ============================================================
# ÉTAPE 3 : Feature Engineering (créer les features pour le modèle)
# ============================================================

# 3a. Combiner TOUTES les infos textuelles en UNE SEULE colonne
# Pourquoi : Le modèle a besoin d'un seul texte par exercice pour le vectoriser
# Exemple résultat : "FORCE INTERMEDIAIRE puissance force explosivité pectoraux triceps INTERMEDIAIRE Aucun"
df['text_features'] = (
    df['type'] + ' ' +           # Ex: "FORCE"
    df['difficulte'] + ' ' +     # Ex: "INTERMEDIAIRE"
    df['objectif'] + ' ' +       # Ex: "puissance force explosivité haut du corps"
    df['muscles_cibles'] + ' ' + # Ex: "pectoraux triceps épaules"
    df['niveauRecommande'] + ' ' + # Ex: "INTERMEDIAIRE"
    df['equipement']             # Ex: "Aucun"
)

# 3b. Encoder les catégories en nombres (le modèle ne comprend pas le texte)
# LabelEncoder transforme : FORCE→1, CARDIO→0, MOBILITE→2, TECHNIQUE→3
le_type = LabelEncoder()
le_diff = LabelEncoder()
df['type_encoded'] = le_type.fit_transform(df['type'])        # FORCE=1, CARDIO=0, etc.
df['difficulte_encoded'] = le_diff.fit_transform(df['difficulte'])  # AVANCE=0, DEBUTANT=1, INTERMEDIAIRE=2

# ============================================================
# ÉTAPE 4 : TF-IDF - Transformer le texte en vecteurs numériques
# ============================================================
# TF-IDF = Term Frequency × Inverse Document Frequency
# - Les mots fréquents dans UN exercice mais rares dans les AUTRES ont plus de poids
# - Exemple : "pliométrie" est rare → poids élevé. "exercice" est commun → poids faible
#
# Paramètres :
# - max_features=150 : garde les 150 mots/bigrammes les plus importants
# - ngram_range=(1,2) : considère les mots seuls ET les paires ("force explosive")
# - min_df=1 : garde même les mots qui n'apparaissent qu'une fois
tfidf = TfidfVectorizer(max_features=150, ngram_range=(1, 2), min_df=1)
tfidf_matrix = tfidf.fit_transform(df['text_features'])
# Résultat : matrice 120 × 150 (120 exercices, 150 features textuelles)
print(f'TF-IDF matrix: {tfidf_matrix.shape}')

# ============================================================
# ÉTAPE 5 : Combiner features textuelles + numériques
# ============================================================

# 5a. Normaliser les features numériques entre 0 et 1
# Pourquoi : Sans normalisation, "calories" (0-300) dominerait "type_encoded" (0-3)
# MinMaxScaler : valeur_normalisée = (valeur - min) / (max - min)
scaler = MinMaxScaler()
numeric_features = scaler.fit_transform(
    df[['intensite_score', 'dureeMinutes', 'calories_estimees', 'type_encoded', 'difficulte_encoded']]
)
# Résultat : matrice 120 × 5 (5 features numériques normalisées)

# 5b. Coller horizontalement : TF-IDF (150 colonnes) + numériques (5 colonnes) = 155 colonnes
# hstack = horizontal stack (colle les matrices côte à côte)
combined_features = hstack([tfidf_matrix, csr_matrix(numeric_features)])
# Résultat : matrice 120 × 155 (chaque exercice est un vecteur de 155 dimensions)
print(f'Combined features: {combined_features.shape}')

# ============================================================
# ÉTAPE 6 : Entraîner le modèle KNN
# ============================================================
# KNN (K-Nearest Neighbors) : trouve les K exercices les plus similaires
#
# Paramètres :
# - n_neighbors=6 : retourne les 6 exercices les plus proches
# - metric='cosine' : utilise la similarité cosinus (mesure l'angle entre vecteurs)
#   → Cosinus est meilleur que euclidien pour du texte car il mesure la DIRECTION, pas la magnitude
# - algorithm='brute' : compare avec TOUS les exercices (dataset petit, pas besoin d'optimisation)
#
# Le modèle ne "prédit" pas — il MÉMORISE tous les exercices et cherche les plus proches
knn_model = NearestNeighbors(n_neighbors=6, metric='cosine', algorithm='brute')
knn_model.fit(combined_features)  # "Entraîner" = mémoriser la matrice de features
print('KNN model trained (K=6, metric=cosine)')

# ============================================================
# ÉTAPE 7 : Exporter le modèle (pour que Flask puisse le charger)
# ============================================================
# joblib.dump() sauvegarde un objet Python dans un fichier binaire
# Flask (app.py) fera joblib.load() au démarrage pour charger ces fichiers
os.makedirs('model', exist_ok=True)  # Créer le dossier model/ s'il n'existe pas

joblib.dump(knn_model, 'model/knn_model.joblib')           # Le modèle KNN entraîné
joblib.dump(tfidf, 'model/tfidf_vectorizer.joblib')        # Le vocabulaire TF-IDF (pour vectoriser les nouvelles requêtes)
joblib.dump(scaler, 'model/scaler.joblib')                 # Le scaler (pour normaliser les nouvelles requêtes avec les mêmes min/max)
joblib.dump(le_type, 'model/label_encoder_type.joblib')    # L'encodeur de type (pour décoder les résultats)
joblib.dump(le_diff, 'model/label_encoder_difficulte.joblib')  # L'encodeur de difficulté
joblib.dump(combined_features, 'model/combined_features.joblib')  # La matrice complète (pour référence)
df.to_csv('model/exercises_enriched.csv', index=False)     # Le dataset enrichi avec les colonnes ajoutées

# ============================================================
# AFFICHAGE FINAL : Liste des fichiers exportés
# ============================================================
print('\nModel exported to model/:')
for f in sorted(os.listdir('model')):
    path = os.path.join('model', f)
    size = os.path.getsize(path) / 1024
    print(f'  {f} ({size:.1f} KB)')

print('\nDone! Start app.py to serve recommendations.')
# Maintenant lancer : python app.py
# Flask chargera ces fichiers et servira les recommandations via l'API REST
