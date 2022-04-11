'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  // BASE_API: '"https://easy-mock.com/mock/5950a2419adc231f356a6636/vue-admin"'
  // BASE_API: '"http://localhost:9001"' // 使用了nginx负载均衡
    BASE_API: '"http://localhost"' // 默认80端口，使用网关的端口访问
})
