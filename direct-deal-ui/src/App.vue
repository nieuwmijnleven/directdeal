<template>
  <v-app>
    <v-app-bar app color="primary" dark> 
      <!-- <v-app-bar-nav-icon /> -->
      <v-app-bar-title>Direct Deal</v-app-bar-title>
    </v-app-bar>

    <v-main>
      <v-slide-x-transition mode="out-in">
        <router-view />
      </v-slide-x-transition>
    </v-main>

    <v-footer height="50" v-if="showBottomNavigation">
      <v-bottom-navigation fixed v-model="selected" class="align-center justify-center">
        <v-btn text value="home" @click="route('/')">
          Home
          <v-icon>mdi-home-variant</v-icon>
        </v-btn>
        <v-btn text value="register-item" @click="route('/register-item')">
          Add Items
          <v-icon>mdi-plus-box</v-icon>
        </v-btn>
        <v-btn text value="chat" @click="route('/chatting-room-list')">
          Chat
          <v-icon>mdi-chat</v-icon>
        </v-btn>
        <v-btn text value="transaction" @click="route('/transaction-history')">
          History
          <v-icon>mdi-account</v-icon>
        </v-btn>
      </v-bottom-navigation>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  name: "App",
  data: () => ({
    
  }),
  computed: {
    showBottomNavigation() {
      return this.$store.getters.getShowBottomNavigation
    },
    selected: {
      get() {
        return this.$store.state.selectedBottomNavigationItem;
      },
      set(value) {
        this.$store.commit('setSelectedBottomNavigationItem', value)
      }
    }
  },
  methods: {
    route(url) {
      if (this.$router.currentRoute.path === url) {
        this.$router.go(0)
      } else {
        this.$router.push(url, () => {})
      }
    }
  }
};
</script>
