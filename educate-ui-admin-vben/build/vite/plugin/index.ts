import { PluginOption } from 'vite'

import vue from '@vitejs/plugin-vue'
import windiCSS from 'vite-plugin-windicss'
import { configThemePlugin } from './theme'

export async function createVitePlugins() {

    const vitePlugins: PluginOption[] = [
        // have to
        vue(),
    ]

    // windiCSS
    vitePlugins.push(windiCSS())

    // vite-plugin-vben-theme
    vitePlugins.push(configThemePlugin(true))

    return vitePlugins
}
