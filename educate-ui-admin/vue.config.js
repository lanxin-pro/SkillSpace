'use strict'
const path = require('path')
const { defineConfig } = require('@vue/cli-service')

// 网页标题
const name = process.env.VUE_APP_TITLE || '纳西妲课堂后台管理系统'


function resolve(dir) {
  return path.join(__dirname, dir)
}
module.exports = defineConfig({
  // 關閉嚴格語法檢查
  lintOnSave: false,
  transpileDependencies: true,
  configureWebpack: {
    name: name,
    resolve: {
      alias: {
        '@': resolve('src')
      }
    },

  },
})
