import { withInstall } from '@/utils'


import appDarkModeToggle from './src/AppDarkModeToggle.vue'
import appLocalePicker from './src/AppLocalePicker.vue'
import appLogo from './src/AppLogo.vue'

export const AppDarkModeToggle = withInstall(appDarkModeToggle)
export const AppLocalePicker = withInstall(appLocalePicker)
export const AppLogo = withInstall(appLogo)
