import { PluginOption } from 'vite'

import vue from '@vitejs/plugin-vue'
import windiCSS from 'vite-plugin-windicss'

export async function createVitePlugins() {

    const vitePlugins: PluginOption[] = [
        // have to
        vue(),
    ]

    // windiCSS
    vitePlugins.push(windiCSS())

    return vitePlugins
}
