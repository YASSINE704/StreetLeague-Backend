"""Train the ML model and export artifacts to model/ directory."""
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.preprocessing import LabelEncoder, MinMaxScaler
from sklearn.neighbors import NearestNeighbors
from scipy.sparse import hstack, csr_matrix
import joblib
import os

# Change to ai-service directory
script_dir = os.path.dirname(os.path.abspath(__file__))
os.chdir(script_dir)

# Load dataset
df = pd.read_csv('data/exercises_dataset.csv')
print(f'Dataset loaded: {df.shape[0]} exercises, {df.shape[1]} columns')

# Feature engineering
df['text_features'] = (
    df['type'] + ' ' + df['difficulte'] + ' ' + df['objectif'] + ' ' +
    df['muscles_cibles'] + ' ' + df['niveauRecommande'] + ' ' + df['equipement']
)

le_type = LabelEncoder()
le_diff = LabelEncoder()
df['type_encoded'] = le_type.fit_transform(df['type'])
df['difficulte_encoded'] = le_diff.fit_transform(df['difficulte'])

# TF-IDF vectorization
tfidf = TfidfVectorizer(max_features=150, ngram_range=(1, 2), min_df=1)
tfidf_matrix = tfidf.fit_transform(df['text_features'])
print(f'TF-IDF matrix: {tfidf_matrix.shape}')

# Combine features
scaler = MinMaxScaler()
numeric_features = scaler.fit_transform(
    df[['intensite_score', 'dureeMinutes', 'calories_estimees', 'type_encoded', 'difficulte_encoded']]
)
combined_features = hstack([tfidf_matrix, csr_matrix(numeric_features)])
print(f'Combined features: {combined_features.shape}')

# Train KNN model
knn_model = NearestNeighbors(n_neighbors=6, metric='cosine', algorithm='brute')
knn_model.fit(combined_features)
print('KNN model trained (K=6, metric=cosine)')

# Export model
os.makedirs('model', exist_ok=True)
joblib.dump(knn_model, 'model/knn_model.joblib')
joblib.dump(tfidf, 'model/tfidf_vectorizer.joblib')
joblib.dump(scaler, 'model/scaler.joblib')
joblib.dump(le_type, 'model/label_encoder_type.joblib')
joblib.dump(le_diff, 'model/label_encoder_difficulte.joblib')
joblib.dump(combined_features, 'model/combined_features.joblib')
df.to_csv('model/exercises_enriched.csv', index=False)

print('\nModel exported to model/:')
for f in sorted(os.listdir('model')):
    path = os.path.join('model', f)
    size = os.path.getsize(path) / 1024
    print(f'  {f} ({size:.1f} KB)')

print('\nDone! Start app.py to serve recommendations.')
