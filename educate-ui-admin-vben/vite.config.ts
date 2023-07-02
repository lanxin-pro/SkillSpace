import { defineConfig } from 'vite'

import { createVitePlugins } from './build/vite/plugin'
/*属于nodejs模块*/
import path from 'path'



// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: {
      '@': path.resolve(__dirname,'src')
    }
  },
  plugins: await createVitePlugins(),
})
