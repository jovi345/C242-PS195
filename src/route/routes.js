const { getCities } = require('../handler/cityHandler')
const {
  getDestinationById,
  searchDestinationByPlaceName,
  recommendPlaceByLastseen,
  getDestinationByCatergoryName,
  getDestinationByCityTag,
} = require('../handler/destinationHandler')
const { registerUser, loginUser } = require('../handler/userHandler')
const { validateToken } = require('../token/token')

const routes = [
  {
    method: 'POST',
    path: '/api/user/register',
    handler: registerUser,
  },
  {
    method: 'POST',
    path: '/api/user/login',
    handler: loginUser,
  },
  {
    method: 'GET',
    path: '/api/destination/place/{id}',
    handler: getDestinationById,
    options: {
      pre: [{ method: validateToken }],
    },
  },
  {
    method: 'POST',
    path: '/api/destination/search',
    handler: searchDestinationByPlaceName,
  },
  {
    method: 'GET',
    path: '/api/destination/recommend/history',
    handler: recommendPlaceByLastseen,
    options: {
      pre: [{ method: validateToken }],
    },
  },
  {
    method: 'GET',
    path: '/api/destination/category/{category}',
    handler: getDestinationByCatergoryName,
  },
  {
    method: 'GET',
    path: '/api/destination/city/{cityTag}',
    handler: getDestinationByCityTag,
  },
  {
    method: 'GET',
    path: '/api/city/{cityTag}',
    handler: getCities,
  },
]

module.exports = routes
