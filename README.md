# Hotrip - Horas Trip App

## Bangkit Capstone Project 2024

Bangkit Capstone Team ID: C242-PS195

## Description
Creating REST APIs and deploying to Google Cloud Platform by using Cloud Run for the API Services and Google Compute Engine for installing MySQL. We use compute Engine for database because it is more cost effective. In making the REST APIs we use Python using the Flask Framework and for responses using JSON format.

## API Documentation

### 1. Recommendation by Searching Place Name

#### Request

| **Method**     | **POST**                          |
| :------------- | :-------------------------------- |
| `Endpoint`     | `/api/destination/recommendation` |
| `Headers`      | `Content-Type: application/json`  |
| `Request Body` | `JSON`                            |

##### Example

```json
{
  "place_name": "Galeri Indonesia Kaya"
}
```

#### Response

| **Method** | **Description** | **Response Body**                                |
| :--------- | :-------------- | ------------------------------------------------ |
| 200        | Success         | `See example below`                              |
| 400        | Bad request     | `{"status": "failed","data": "Place not found"}` |

```json
[
  {
    "category": "budaya_sejarah",
    "city": "Kota Medan",
    "city_tag": "medan",
    "cluster": 1,
    "description": "Museum kecil yang mencatat sejarah dan budaya lokal, dengan artefak suku dan tur berpemandu.",
    "id": 54,
    "image_url": "https://storage.googleapis.com/travelease-bucket/medan/museum_negeri_sumatera_utara.jpg",
    "lat": "3.5681315",
    "lng": "98.6963665",
    "phone": "-",
    "place_name": "Museum Negeri Sumatera Utara",
    "rating": 4.5,
    "reviews_count": 2713,
    "state": "Sumatera Utara"
  },
  {
    // ...
  }
]
```

### 2. Recommendation by Location Chosen

#### Request

| **Method** | **GET**                                         |
| :--------- | :---------------------------------------------- |
| `Endpoint` | `/api/destination/recommendation-cb/{location}` |

#### Example

```
{{baseUrl}}/api/destination/recommendation-cb/lombok
```

#### Response

| **Method** | **Description** | **Response Body**                                              |
| :--------- | :-------------- | -------------------------------------------------------------- |
| 200        | Success         | `See example below`                                            |
| 400        | Bad request     | `{"status": "failed","data": "Location is not available yet"}` |

```json
[
  {
    "category": "alam",
    "city": "Kabupaten Lombok Timur",
    "city_tag": "lombok",
    "cluster": 0,
    "description": "Tempat berkemah sebelum menuju Puncak Rinjani keesokan paginya. Terdapat pemandangan matahari terbenam yang indah menghadap danau dan lereng huruf E yang terkenal menuju Puncak Rinjani",
    "id": 109,
    "image_url": "https://storage.googleapis.com/travelease-bucket/lombok/pelawangan_sembalun.jpg",
    "lat": "-8.3938231",
    "lng": "116.4404638",
    "phone": "-",
    "place_name": "Pelawangan Sembalun",
    "rating": 4.8,
    "reviews_count": 333,
    "state": "Nusa Tenggara Bar."
  },
  {
    // ...
  }
]
```

### 3. Recommendation by Survey Result

#### Request

| **Method** | **POST**                                 |
| :--------- | :--------------------------------------- |
| `Endpoint` | `/api/destination/survey-recommendation` |

#### Example

```json
{
  "mbti": "ENTJ",
  "location": "lombok",
  "preffered_category": "tujuan_wisata",
  "travel_style": "partner",
  "age": "21",
  "travel_frequency": "1"
}
```

#### Response

| **Method** | **Description** | **Response Body**   |
| :--------- | :-------------- | ------------------- |
| 200        | Success         | `See example below` |

```json
[
  {
    "category": "Alam",
    "city": "Kabupaten Lombok Tengah",
    "city_tag": "lombok",
    "cluster": 0,
    "description": "Perlu tiket untuk melihat 2 air terjun yang mengalir di atas tebing yang tertutup pakis di area hutan.",
    "id": 104,
    "image_url": "https://storage.googleapis.com/travelease-bucket/lombok/benang_stokel_and_benang_kelambu_waterfall.jpg",
    "lat": "-8.533019",
    "lng": "116.3413827",
    "phone": "+62 819-4980-0700",
    "place_name": "Benang Stokel and Benang Kelambu Waterfall",
    "rating": 4.3,
    "reviews_count": 1286,
    "state": "Nusa Tenggara Bar."
  },
  {
    // ...
  }
]
```

## API Installation Guide

Model API

```bash
git clone -b cc-py https://github.com/jovi345/C242-PS195.git model-api
cd model-api
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

Create .env file

```bash
DB_HOST=<your_db_host>
DB_USERNAME=<your_db_username>
DB_PASSWORD=<your_db_password>
DATABASE=<your_db_name>
```

Run the server on port :8080

```bash
python3 main.py
```
