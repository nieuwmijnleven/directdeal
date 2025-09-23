<template>
  <v-container fluid justify-center v-if="isLoaded">
    <v-card flat class="mx-auto">
      <v-list two-line v-for="item in items" :key="item.id" class="my-0 py-0">
          <v-list-item @click="selectItem(item.id)">
            <v-list-item-avatar size="100" class="rounded" >
              <v-img :src="'/api/v1/image/' + item.mainImage"></v-img>
            </v-list-item-avatar>
            <v-list-item-content class="mb-7">
              <v-list-item-title>{{item.title}}</v-list-item-title>
              <v-list-item-subtitle class="mt-1">
                {{item.targetPrice}} USD
              </v-list-item-subtitle>
              <v-list-item-subtitle class="mt-1">
                <v-chip x-small>
                  {{item.category}}
                </v-chip>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider></v-divider>
      </v-list>
    </v-card>
  </v-container>
  <v-container fluid fill-height justify-center v-else>
    <div class="text-center">
      <v-progress-circular
        :size="70"
        color="primary"
        indeterminate
      ></v-progress-circular>
    </div>
  </v-container>
</template>

<script>
//import axios from 'axios'
import api from '../axios'

export default {
  data: () => ({
    isLoaded: false,
    items: []
  }),
  methods: {
    async fetchSaleList() {
      this.isLoaded = false
      try {     
        let response = await api({
          method: 'GET',
          url: '/api/v1/salelist?page=0&size=10&sort=createdDate,desc', 
        })

        this.items = response.data
        this.isLoaded = true
      } catch(error) {
        console.warn("Error: ", error)
      }
    },
    selectItem(itemId) {
      this.$store.commit('setRouterParams', {path: '/item-detail', value: {id: itemId}})
      this.$router.push('/item-detail')
    }
  },
  mounted() {
    this.$store.commit("setShowBottomNavigation", true)
    this.$store.commit("setSelectedBottomNavigationItem", 'home')
    //axios.defaults.headers.common['Authorization'] = this.$store.state.authorization
    this.fetchSaleList()
  },
};
</script>
