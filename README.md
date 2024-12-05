# Hotrip - Horas Trip App

## Bangkit Capstone Project 2024

Bangkit Capstone Team ID: C242-PS195

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
