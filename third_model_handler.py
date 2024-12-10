from flask import Flask, request, jsonify
import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.preprocessing import OneHotEncoder, StandardScaler
from sklearn.metrics.pairwise import cosine_similarity

MODEL_URL_3 = "./models/model_survey.h5"

model = tf.keras.models.load_model(
    MODEL_URL_3, custom_objects={"mse": tf.keras.losses.MeanSquaredError()}
)

df = pd.read_csv("./datasets/survey_result.csv")

app = Flask(__name__)

categorical_features = ["mbti", "location", "preffered_category", "travel_style"]
encoder = OneHotEncoder(sparse_output=False)
encoder.fit(df[categorical_features])


def recommendPlaceBySurveyResult(user_data):
    encoded_categorical = encoder.transform(df[categorical_features])

    encoded_df = pd.DataFrame(
        encoded_categorical,
        columns=encoder.get_feature_names_out(categorical_features),
    )

    encoded_df["age"] = df["age"]
    encoded_df["travel_frequency"] = df["travel_frequency"]

    scaler = StandardScaler()
    encoded_df[["age", "travel_frequency"]] = scaler.fit_transform(
        encoded_df[["age", "travel_frequency"]]
    )

    user_vectors = model.predict(encoded_df)

    new_user_data = {
        "mbti": [user_data["mbti"]],
        "location": [user_data["location"]],
        "preffered_category": [user_data["preffered_category"]],
        "travel_style": [user_data["travel_style"]],
        "age": [int(user_data["age"])],
        "travel_frequency": [int(user_data["travel_frequency"])],
    }

    new_user_df = pd.DataFrame(new_user_data)
    encoded_new_user = encoder.transform(new_user_df[categorical_features])
    encoded_new_user = np.concatenate(
        [encoded_new_user, scaler.transform(new_user_df[["age", "travel_frequency"]])],
        axis=1,
    )
    new_user_vector = model.predict(encoded_new_user)

    similarities = cosine_similarity(new_user_vector, user_vectors)
    top_5 = np.argsort(similarities[0])[-50:][::-1]
    count = 0
    result = []

    for i in top_5:
        if df["location"].iloc[i] == new_user_data["location"][0] and count < 5:
            place_1 = int(df["place_visited_1"].fillna(0).iloc[i])
            place_2 = int(df["place_visited_2"].fillna(0).iloc[i])
            place_3 = int(df["place_visited_3"].fillna(0).iloc[i])
            place_4 = int(df["place_visited_4"].fillna(0).iloc[i])
            place_5 = int(df["place_visited_5"].fillna(0).iloc[i])
            result.append([place_1, place_2, place_3, place_4, place_5])
            count += 1

    unique_values = list(
        set(value for sublist in result for value in sublist if value != 0)
    )

    return unique_values
