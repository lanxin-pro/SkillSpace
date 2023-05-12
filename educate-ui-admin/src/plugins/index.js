import modal from './modal.js'

export default {

    install(app,options) {
        app.config.globalProperties.$modal = modal
    }
}
