# Hotrip - Horas Trip App - CLOUD COMPUTING

## Bangkit Capstone Project 2024H2

## Description
Creating REST APIs and deploying to Google Cloud Platform by using Cloud run for the API Services and Google Compute Engine for installing MySQL. We use compute Engine for database because it is more cost effective

## RESTful APIs
In making the RESTful APIs we use Javascript using the Hapi Js and for responses using JSON format.

## API Documentation

### 1. Register User

#### Request

| **Method**     | **POST**                         |
| :------------- | :------------------------------- |
| `Endpoint`     | `/api/user/register`             |
| `Headers`      | `Content-Type: application/json` |
| `Request Body` | `JSON`                           |

##### Example

```json
{
  "username": "John Doe",
  "email": "johndoe@gmail.com",
  "password": "johndoesecret",
  "confirmPassword": "johndoesecret",
  "userLocation": "Jakarta"
}
```

#### Response

| **Method** | **Description**      | **Response Body**                                                         |
| :--------- | :------------------- | ------------------------------------------------------------------------- |
| 201        | Success              | `{"status": "success","message": "User has been successfully created"}`   |
| 400        | Bad request          | `{"status": "failed","data": "Email has been used for registration"}`     |
| 422        | Unprocessable Entity | `{"status": "failed", "message": "\"email\" is not allowed to be empty"}` |

### 2. Login User

#### Request

| **Method**     | **POST**                         |
| :------------- | :------------------------------- |
| `Endpoint`     | `/api/user/login`                |
| `Headers`      | `Content-Type: application/json` |
| `Request Body` | `JSON`                           |

##### Example

```json
{
  "email": "johndoe@gmail.com",
  "password": "johndoesecret"
}
```

#### Response

| **Method** | **Description**        | **Response Body**                                                                 |
| :--------- | :--------------------- | --------------------------------------------------------------------------------- |
| 200        | Success                | `{"status": "success","token":"this-is-a-secret-token"}`                          |
| 400        | Bad request (Password) | `{"status": "failed","message": "Wrong Password"} or  `                           |
| 400        | Bad request (Email)    | `{"status": "failed","message": "Account is not found. Email is not registered"}` |
| 422        | Unprocessable Entity   | `{"status": "failed", "message": "\"email\" is not allowed to be empty"}`         |

### 3. Place Recommendations Based on Last Seen

#### Request

| **Method** | **GET**                              |
| :--------- | :----------------------------------- |
| `Endpoint` | `/api/destination/recommend/history` |

#### Headers

| **Key**       | **Value**            | **Description**              |
| :------------ | :------------------- | :--------------------------- |
| Authorization | `Bearer <JWT_TOKEN>` | JWT token for authentication |

#### Pre-requisite

| **Step**        | **Description**                                                          |
| --------------- | ------------------------------------------------------------------------ |
| `validateToken` | Middleware to validate the JWT token. Ensures the user is authenticated. |

#### Response

| **Status Code** | **Description**                      | **Response Body** |
| --------------- | ------------------------------------ | ----------------- |
| 200             | Recommendations fetched successfully | See example below |

##### Response Example

```json
{
  "status": "success",
  "data": [
    {
      "category": "Wisata Budaya dan Sejarah",
      "city": "Kota Jakarta Barat",
      "city_tag": "jakarta",
      "cluster": 4,
      "description": "Museum yang menampilkan sejarah perbankan lokal, termasuk mata uang antik dan uang kertas internasional.",
      "id": 30,
      "image_url": "https://lh5.googleusercontent.com/p/AF1QipOKS6m4idJIhAlg02WwUkMaRzCHZlkhwjtA5qh6=w675-h390-n-k-no",
      "lat": "-6.1371066",
      "lng": "106.8129846",
      "phone": "+62 812-9157-3940",
      "place_name": "Museum Bank Indonesia",
      "rating": 4.7,
      "reviews_count": 10637,
      "state": "Daerah Khusus Ibukota Jakarta"
    },
    {
      // ...
    }
  ]
}
```

### 4. Destinations by Category

#### Request

| **Method** | **GET**                                |
| :--------- | :------------------------------------- |
| `Endpoint` | `/api/destination/category/{category}` |

#### Response

| **Status Code** | **Description**           | **Response Body**                                                        |
| --------------- | ------------------------- | ------------------------------------------------------------------------ |
| 200             | Data fetched successfully | See example below                                                        |
| 400             | Bad request               | {"status": "failed","message": "No place was found with that category" } |

##### Response Example

```json
{
  "status": "success",
  "data": [
    {
      "id": 2,
      "place_name": "Taman Nasional Kepulauan Seribu",
      "state": "Daerah Khusus Ibukota Jakarta",
      "city": "Kab. Administrasi Kepulauan Seribu",
      "city_tag": "jakarta",
      "phone": "-",
      "category": "alam",
      "description": "Penyu, snorkeling dan menyelam di taman alam yang tersebar di Kepulauan Seribu.",
      "image_url": "https://storage.googleapis.com/travelease-bucket/jakarta/taman_nasional_kepulauan_seribu.jpg",
      "reviews_count": 555,
      "rating": 4.6,
      "lat": "-5.7453663",
      "lng": "106.6151821",
      "cluster": 4
    },
    {
      // ...
    }
  ]
}
```

### 5. Search Place by Place Name

#### Request

| **Method**     | **GET**                                  |
| :------------- | :--------------------------------------- |
| `Endpoint`     | `/api/destination/search?q=<place name>` |
| `Headers`      | `Content-Type: application/json`         |
| `Request Body` | `JSON`                                   |

#### Response

| **Status Code** | **Description**           | **Response Body**                                     |
| --------------- | ------------------------- | ----------------------------------------------------- |
| 200             | Data fetched successfully | See example below                                     |
| 400             | Bad request               | {"status": "failed","message": "Place is not found" } |

##### Example

{{baseUrl}}/api/destination/search?q=taman mini

##### Response Example

```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "place_name": "Taman Mini Indonesia Indah",
      "state": "Daerah Khusus Ibukota Jakarta",
      "city": "Kota Jakarta Timur",
      "city_tag": "jakarta",
      "phone": "+62 804 178 9789",
      "category": "rekreasi_keluarga",
      "description": "Taman yang memamerkan budaya dan alam Indonesia melalui museum, taman, dan replika pemandangan terkenal.",
      "image_url": "https://storage.googleapis.com/travelease-bucket/jakarta/taman_mini_indonesia_indah.jpg",
      "reviews_count": 164271,
      "rating": 4.6,
      "lat": "-6.3020386",
      "lng": "106.8898924",
      "cluster": 8
    },
    {
      // ...
    }
  ]
}
```

### 6. Destinations by City

#### Request

| **Method** | **GET**                        |
| :--------- | :----------------------------- |
| `Endpoint` | `/api/destination/city/{city}` |

#### Response

| **Status Code** | **Description**           | **Response Body**                                     |
| --------------- | ------------------------- | ----------------------------------------------------- |
| 200             | Data fetched successfully | See example below                                     |
| 400             | Bad request               | {"status": "failed","message": "Place is not found" } |

##### Response Example

```json
{
  "status": "success",
  "data": [
    {
      "id": 52,
      "place_name": "Tjong A Fie Mansion",
      "state": "Sumatera Utara",
      "city": "Kota Medan",
      "city_tag": "medan",
      "phone": "+62 811-6130-388",
      "category": "tujuan_wisata",
      "description": "Dibangun pada 1895, rumah pengusaha ternama Tjong A Fie yang dipugar, menyediakan galeri dan tur berpemandu.",
      "image_url": "https://storage.googleapis.com/travelease-bucket/medan/tjong_a_fie_mansion.jpg",
      "reviews_count": 3145,
      "rating": 4.6,
      "lat": "3.5853631",
      "lng": "98.6802302",
      "cluster": 1
    },
    {
      // ...
    }
  ]
}
```

### 7. Destinations by ID

#### Request

| **Method** | **GET**                       |
| :--------- | :---------------------------- |
| `Endpoint` | `/api/destination/place/{id}` |

#### Response

| **Status Code** | **Description**           | **Response Body**                                                  |
| --------------- | ------------------------- | ------------------------------------------------------------------ |
| 200             | Data fetched successfully | See example below                                                  |
| 400             | Bad request               | {"status": "failed","message": "No place was found with that ID" } |

##### Response Example

```json
{
  "status": "success",
  "data": {
    "id": 1,
    "place_name": "Taman Mini Indonesia Indah",
    "state": "Daerah Khusus Ibukota Jakarta",
    "city": "Kota Jakarta Timur",
    "city_tag": "jakarta",
    "phone": "+62 804 178 9789",
    "category": "rekreasi_keluarga",
    "description": "Taman yang memamerkan budaya dan alam Indonesia melalui museum, taman, dan replika pemandangan terkenal.",
    "image_url": "https://storage.googleapis.com/travelease-bucket/jakarta/taman_mini_indonesia_indah.jpg",
    "reviews_count": 164271,
    "rating": 4.6,
    "lat": "-6.3020386",
    "lng": "106.8898924",
    "cluster": 8
  }
}
```

### 8. Grouping by Region

#### Request

| **Method** | **GET**               |
| :--------- | :-------------------- |
| `Endpoint` | `/api/city/{cityTag}` |

#### Example

{{baseUrl}}/api/city/jakarta

#### Response

| **Status Code** | **Description**           | **Response Body**                                                  |
| --------------- | ------------------------- | ------------------------------------------------------------------ |
| 200             | Data fetched successfully | See example below                                                  |
| 400             | Bad request               | {"status": "failed","message": "No place was found with that ID" } |

##### Response Example

```json
{
  "status": "success",
  "data": [
    {
      "id": 4,
      "name": "Jakarta",
      "tag": "jakarta",
      "url": "https://storage.googleapis.com/travelease-bucket/city/jakarta.jpeg",
      "region": 3
    },
    {
      "id": 7,
      "name": "Yogyakarta",
      "tag": "yogyakarta",
      "url": "https://storage.googleapis.com/travelease-bucket/city/yogyakarta.jpeg",
      "region": 3
    }
  ]
}
```

## API Installation Guide

User & Destination API

```bash
git clone -b cloud-computing https://github.com/jovi345/C242-PS195.git user-destination-api
cd user-destination-api
npm install
```

Create .env file

```bash
JWT_SECRET_KEY=<input_random_key>
HOST=<your_db_url>
USERNAME=<your_db_username>
PASSWORD=<your_db_password>
DATABASE=<your_db_name>
TARGET_URL=<url_from_running_the_model_api (localhost:8080)>
```

Run the server on port :8081 (Kindly change the port to 8081 on server.js)

```bash
npm run start
```

## Cloud Architecture

![hotrip architecture drawio (6)](https://github.com/user-attachments/assets/051882ee-3d5a-4981-91c9-3ea2eb106b96)
