import { defineConfig } from 'vite'

import { createVitePlugins } from './build/vite/plugin'
/*属于nodejs模块*/
import { resolve } from 'path'
import { generateModifyVars } from './build/generate/generateModifyVars'

function pathResolve(dir: string) {
  return resolve(process.cwd(), '.', dir)
}

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: [
      // @/xxxx => src/xxxx
      {
        find: /\@\//,
        replacement: pathResolve('src') + '/'
      }
    ]
  },
  plugins: await createVitePlugins(),
  // 引用其他的css
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: generateModifyVars(),
        javascriptEnabled: true
      }
    }
  },
})
