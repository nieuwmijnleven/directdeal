import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userId:'',
    authorization: '',
    showBottomNavigation: true,
    selectedBottomNavigationItem: 'home',
    routerParams: {},
    readMessageCountMap: {}
  },
  mutations: {
    setUserId(state, id) {
      state.userId = id
    },
    setAuthorization(state, auth) {
      state.authorization = auth
    },
    setShowBottomNavigation(state, visible) {
      state.showBottomNavigation = visible
    },
    setSelectedBottomNavigationItem(state, selected) {
      state.selectedBottomNavigationItem = selected
    },
    setRouterParams(state, params) {
      state.routerParams = params
    },
    setReadMessageCountMap(state, entry) {
      console.log("setReadMessageCountMap " + entry.id + "," + entry.count)
      state.readMessageCountMap[entry.id] = entry.count
    }
  },
  getters: {
    getAuthorization(state) {
      return state.authorization
    },
    getShowBottomNavigation(state) {
      return state.showBottomNavigation
    },
    getReadMessageCountMap(state, chattingRoomId) {
      return state.readMessageCountMap[chattingRoomId]
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
