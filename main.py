from flask import jsonify, request, Flask
from flask_cors import CORS

from db_connection import get_connection
from first_model_handler import (
    recommend_place,
    recommend_placeById,
    data,
    first_model,
    tfidf,
)
from second_model_handler import get_similar_places
from third_model_handler import recommendPlaceBySurveyResult
import random


app = Flask(__name__)
CORS(app)


@app.route("/api/destination/recommendation", methods=["POST"])
def recommendByPlaceName():
    user_input = request.get_json()
    place_name = user_input.get("place_name", "")

    if not place_name:
        return jsonify({"error": "Input place_name is required"}), 400

    recommendations = recommend_place(place_name, data, first_model, tfidf)
    recommendation_result = recommendations.to_dict(orient="records")

    arr = []
    for recomendation in recommendation_result:
        arr.append(recomendation["place_name"])

    place_names_str = "', '".join(arr)
    query = f"SELECT * FROM destinations WHERE place_name IN ('{place_names_str}') AND rating >= 4.0;"
    connection = get_connection()
    cursor = connection.cursor()
    cursor.execute(query)
    rows = cursor.fetchall()
    columns = [desc[0] for desc in cursor.description]
    result = [dict(zip(columns, row)) for row in rows]
    cursor.close()
    connection.close()

    return jsonify(result)


@app.route("/api/destination/recommendation-cb/<location>", methods=["GET"])
def recommendPlaceByLocation(location):
    top_n = 100

    location_to_ids = {
        "jakarta": [random.randint(1, 50)],
        "bali": [random.randint(294, 474)],
        "medan": [random.randint(50, 71)],
        "padang": [random.randint(78, 100)],
        "batam": [random.randint(232, 293)],
        "yogyakarta": [random.randint(475, 574)],
        "lombok": [random.randint(102, 231)],
    }

    ids = location_to_ids.get(location, [])
    id = random.choice(ids) if ids else None

    place_names = get_similar_places(id, top_n)
    place_names_str = "', '".join(place_names)
    query = f"SELECT * FROM destinations WHERE place_name IN ('{place_names_str}') AND city_tag='{location}';"
    connection = get_connection()
    cursor = connection.cursor()
    cursor.execute(query)
    rows = cursor.fetchall()
    columns = [desc[0] for desc in cursor.description]
    result = [dict(zip(columns, row)) for row in rows]
    cursor.close()
    connection.close()

    return jsonify(result)


@app.route("/api/destination/recommendation-cb/history/<idx>", methods=["GET"])
def recommendByPlaceId(idx):
    recommendations = recommend_placeById(int(idx), data, first_model, tfidf)

    recommendation_result = recommendations.to_dict(orient="records")

    arr = []
    for recomendation in recommendation_result:
        arr.append(recomendation["place_name"])

    place_names_str = "', '".join(arr)
    query = f"SELECT * FROM destinations WHERE place_name IN ('{place_names_str}');"
    connection = get_connection()
    cursor = connection.cursor()
    cursor.execute(query)
    rows = cursor.fetchall()
    columns = [desc[0] for desc in cursor.description]
    result = [dict(zip(columns, row)) for row in rows]
    cursor.close()
    connection.close()

    return jsonify(result)


@app.route("/api/destination/survey-recommendation", methods=["POST"])
def recommendBySurveyResult():
    user_data = request.json
    idx = recommendPlaceBySurveyResult(user_data)

    placeholders = ", ".join(map(str, idx))
    order_by_case = "\n".join([f"WHEN id = {id} THEN {i}" for i, id in enumerate(idx)])

    query = f"""
    SELECT * 
    FROM destinations 
    WHERE id IN ({placeholders}) 
    ORDER BY 
        CASE 
            {order_by_case} 
        END;
    """
    connection = get_connection()
    cursor = connection.cursor()
    cursor.execute(query)
    rows = cursor.fetchall()
    columns = [desc[0] for desc in cursor.description]
    result = [dict(zip(columns, row)) for row in rows]
    cursor.close()
    connection.close()

    return jsonify(result)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)
