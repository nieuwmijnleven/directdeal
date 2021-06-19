import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    authorization: '',
    showBottomNavigation: true,
    selectedBottomNavigationItem: 'home'
  },
  mutations: {
    setAuthorization(state, auth) {
      state.authorization = auth
    },
    setShowBottomNavigation(state, visible) {
      state.showBottomNavigation = visible
    },
    setSelectedBottomNavigationItem(state, selected) {
      state.selectedBottomNavigationItem = selected;
    }
  },
  getters: {
    getAuthorization(state) {
      return state.authorization
    },
    getShowBottomNavigation(state) {
      return state.showBottomNavigation
    }
  },
  actions: {
  },
  modules: {
  },
  plugins: [
    createPersistedState()
  ],
})