require('dotenv').config()

const Hapi = require('@hapi/hapi')
const routes = require('./src/route/routes')

const init = async () => {
  const server = Hapi.server({
    port: 8081,
    host: '0.0.0.0',
    routes: {
      cors: {
        origin: ['*'],
      },
    },
  })

  server.route(routes)

  await server.start()
  console.log(`server is running on ${server.info.uri} `)
}

init()
