import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userId:'',
    accessToken:'',
    //authorization: '',
    showBottomNavigation: true,
    selectedBottomNavigationItem: 'home',
    routerParams: {},
    readMessageCountMap: {}
  },
  mutations: {
    setUserId(state, id) {
      state.userId = id
    },
    setAccessToken(state, token) {
      state.accessToken = token
    },
//    setAuthorization(state, auth) {
//      state.authorization = auth
//    },
    setShowBottomNavigation(state, visible) {
      state.showBottomNavigation = visible
    },
    setSelectedBottomNavigationItem(state, selected) {
      state.selectedBottomNavigationItem = selected
    },
    setRouterParams(state, entry) {
      console.log("setRouterParams " + entry.path + "," + entry.value)
      state.routerParams[entry.path] = entry.value
    },
    setReadMessageCountMap(state, chatInfo) {
      console.log("setReadMessageCountMap " + chatInfo.id + "," + chatInfo.count)
      const {roomId, readMessageCount} = chatInfo;
      if (!state.readMessageCountMap[state.userId]) state.readMessageCountMap[state.userId] = {}
      state.readMessageCountMap[state.userId][roomId] = readMessageCount
    }
  },
  getters: {
    getAccessToken(state) {
      return state.accessToken
    },
//    getAuthorization(state) {
//      return state.authorization
//    },
    getShowBottomNavigation(state) {
      return state.showBottomNavigation
    },
    getReadMessageCountMap(state, chattingRoomId) {
      return state.readMessageCountMap[state.userId][chattingRoomId]
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
