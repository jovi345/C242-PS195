import tensorflow as tf
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer


file_path = "./datasets/destinations_db.csv"
data = pd.read_csv(file_path)

MODEL_URL = "./models/tourism_recommendation_model.h5"

first_model = tf.keras.models.load_model(MODEL_URL)

data["combined_features"] = data["category"] + " " + data["description"]

tfidf = TfidfVectorizer(stop_words="english")
tfidf_matrix = tfidf.fit_transform(data["combined_features"])


def recommend_place(input_place, data, model_1, tfidf):
    idx = data[data["place_name"] == input_place].index[0]
    input_features = tfidf.transform([data.loc[idx, "combined_features"]]).toarray()
    predictions = model_1.predict(input_features)
    recommended_idx = np.argsort(predictions[0])[-6:-1]
    recommended_places = data.iloc[recommended_idx][
        ["id", "place_name", "rating", "reviews_count"]
    ]
    return recommended_places


def recommend_placeById(idx, data, model_1, tfidf):
    input_features = tfidf.transform([data.loc[idx, "combined_features"]]).toarray()
    predictions = model_1.predict(input_features)
    recommended_idx = np.argsort(predictions[0])[-6:-1]
    recommended_places = data.iloc[recommended_idx][
        ["id", "place_name", "rating", "reviews_count"]
    ]
    return recommended_places
