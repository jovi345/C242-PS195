const dbConfig = require('../db/db')
const axios = require('axios')

const getDestinationById = async (request, h) => {
  const { id } = request.params
  const searchQuery = 'SELECT * FROM destinations WHERE id = ?'
  const [destinationRow] = await dbConfig.query(searchQuery, [id])

  const authUser = request.auth.credentials
  const { email } = authUser
  const lastSeenQueryUpdate = 'UPDATE users SET last_seen = ? WHERE email = ?'
  const [userRow] = await dbConfig.query(lastSeenQueryUpdate, [id, email])

  if (userRow.affectedRows != 1) {
    const response = h.response({
      status: 'Failed',
      status: 'System failure',
    })
    response.code(500)
    return response
  }

  if (destinationRow.length != 1) {
    const response = h.response({
      status: 'failed',
      message: 'No place was found with that ID',
    })
    response.code(400)
    return response
  }

  const response = h.response({
    status: 'success',
    data: destinationRow[0],
  })
  response.code(200)
  return response
}

const searchDestinationByPlaceName = async (request, h) => {
  const { q } = request.query
  const place_name = `%${q}%`

  const searchQuery = 'SELECT * FROM destinations WHERE place_name LIKE ?'
  const [row] = await dbConfig.query(searchQuery, [place_name])

  if (row.length == 0) {
    const response = h.response({
      status: 'failed',
      message: 'Place is not found',
    })
    response.code(400)
    return response
  }

  const response = h.response({
    status: 'success',
    data: row,
  })
  return response
}

const recommendPlaceByLastseen = async (request, h) => {
  const authUser = request.auth.credentials
  const { email } = authUser

  const query = 'SELECT * FROM users WHERE email = ?'
  const [row] = await dbConfig.query(query, [email])

  const id = row[0].last_seen - 1

  const targetUrl = `${process.env.TARGET_URL}/${id}`

  const dataResponse = await axios.get(targetUrl)

  const { data } = dataResponse

  const response = h.response({
    status: 'success',
    data,
  })
  response.code(200)
  return response
}

const getDestinationByCatergoryName = async (request, h) => {
  const { category } = request.params

  const categoryQuery = 'SELECT * FROM destinations WHERE category=?'
  const [row] = await dbConfig.query(categoryQuery, [category])

  if (row.length === 0) {
    const response = h.response({
      status: 'failed',
      message: 'No place was found with that category',
    })
    response.code(400)
    return response
  }

  const response = h.response({
    status: 'success',
    data: row,
  })
  response.code(200)
  return response
}

const getDestinationByCityTag = async (request, h) => {
  const { cityTag } = request.params
  const getcityquery = 'SELECT * FROM destinations WHERE city_tag=?'
  const [row] = await dbConfig.query(getcityquery, [cityTag])

  if (row.length === 0) {
    const response = h.response({
      status: 'failed',
      message: 'No destinations found with such name',
    })
    response.code(400)
    return response
  }

  const response = h.response({
    status: 'success',
    data: row,
  })
  response.code(200)
  return response
}

module.exports = {
  getDestinationById,
  searchDestinationByPlaceName,
  recommendPlaceByLastseen,
  getDestinationByCatergoryName,
  getDestinationByCityTag,
}
