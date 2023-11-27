import Vue from "vue"
import Vuex from "vuex"
import getters from "./getters.js"
import modules from "./modules/index.js"

Vue.use(Vuex)
const debug = process.env.NODE_ENV !== "production"

export default new Vuex.Store({
    modules,
    getters,
    strict: debug
});
