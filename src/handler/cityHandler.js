const dbConfig = require('../db/db')

const getCities = async (request, h) => {
  try {
    const { cityTag } = request.params

    const query =
      'SELECT * FROM cities WHERE tag = ? OR region = (SELECT region FROM cities WHERE tag = ?)'
    const [rows] = await dbConfig.query(query, [cityTag, cityTag])

    const response = h.response({
      status: 'success',
      data: rows,
    })
    response.code(200)
    return response
  } catch (error) {
    const response = h.response({
      status: 'failed',
      message: 'System failure',
    })
    response.code(500)
    return response
  }
}

module.exports = { getCities }
