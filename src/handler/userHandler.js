const bcrypt = require('bcrypt')
const { nanoid } = require('nanoid')
const { generateToken } = require('../token/token')
const { registerSchema, loginSchema } = require('../validation/schema')
const dbConfig = require('../db/db')

const registerUser = async (request, h) => {
  const { error } = registerSchema.validate(request.payload, {
    abortEarly: false,
  })
  if (error) {
    const response = h.response({
      status: 'failed',
      message: error.message,
    })
    response.code(422)
    return response
  }

  const { username, email, password, confirmPassword, userLocation } =
    request.payload
  if (password !== confirmPassword) {
    const response = h.response({
      status: 'fail',
      data: 'Password does not match',
    })
    response.code(400)
    return response
  }
  const id = nanoid()
  const role = 'user'
  const created_at = new Date()
  const updated_at = created_at

  const salt = await bcrypt.genSalt(10)
  const hashed_password = await bcrypt.hash(password, salt)

  const createQuery =
    'INSERT INTO users (id, username, email, hashed_password, role, created_at, updated_at, user_location) VALUES(?, ?, ?, ?, ?, ?, ?, ?)'
  try {
    const checkUserQuery = 'SELECT * FROM users WHERE email = ?'
    const [existingUser] = await dbConfig.query(checkUserQuery, [email])

    if (existingUser.length > 0) {
      const response = h.response({
        status: 'failed',
        data: 'Email has been used for registration',
      })
      response.code(400)
      return response
    }

    const [result] = await dbConfig.query(createQuery, [
      id,
      username,
      email,
      hashed_password,
      role,
      created_at,
      updated_at,
      userLocation,
    ])

    if (result.affectedRows === 1) {
      const response = h.response({
        status: 'success',
        message: 'User has been successfully created',
      })
      response.code(200)
      return response
    }

    const response = h.response({
      status: 'failed',
      message: 'Systems failure',
    })
    response.code(500)
    return response
  } catch (error) {
    console.log(error)
    if (error.code === 'ER_DUP_ENTRY') {
      const response = h.response({
        data: 'Email has been used for registration',
      })
      response.code(400)
      return response
    }
    const response = h.response({
      data: error,
    })
    response.code(500)
    return response
  }
}

const loginUser = async (request, h) => {
  try {
    const { error } = loginSchema.validate(request.payload, {
      abortEarly: false,
    })
    if (error) {
      const response = h.response({
        status: 'failed',
        message: error.message,
      })
      response.code(422)
      return response
    }

    const { email, password } = request.payload

    const searchQuery = 'SELECT * FROM users WHERE email = ?'
    const [row] = await dbConfig.query(searchQuery, [email])

    if (!row.length) {
      const response = h.response({
        status: 'failed',
        message: 'Account is not found. Email is not registered',
      })
      response.code(400)
      return response
    }

    const user = row[0]
    const { username, user_location, hashed_password } = user
    const isPasswordMatched = await bcrypt.compare(password, hashed_password)

    if (!isPasswordMatched) {
      const response = h.response({
        status: 'failed',
        message: 'Wrong password',
      })
      response.code(400)
      return response
    }

    const token = generateToken(user)

    const response = h.response({
      status: 'success',
      loginResult: {
        email,
        username,
        user_location,
        token,
      },
    })
    response.code(200)
    return response
  } catch (error) {
    console.log(error)
    const response = h.response({
      status: 'failed',
      message: error,
    })
    response.code(500)
    return response
  }
}

module.exports = {
  registerUser,
  loginUser,
}
