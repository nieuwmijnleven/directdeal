<template>
  <v-container fluid justify-center v-if="isLoaded">
    <v-card flat class="mx-auto">
      <v-list two-line class="my-0 py-0">
          <v-list-item>
            <v-carousel hide-delimiter-background>
              <v-carousel-item v-for="(image, index) in item.images" :key="index">
                <v-img :src="'http://localhost:8084/api/v1/image/' + image"></v-img>
              </v-carousel-item>
            </v-carousel>
          </v-list-item>
          <v-divider/>
          <v-list-item>
            <v-list-item-avatar color="green white--text">
              {{item.ownerId[0]}}
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title>{{item.ownerId}}</v-list-item-title>
            </v-list-item-content>
            <v-list-item-action>
              <v-btn color="primary dark" @click="startDeal(item)">Start Deal</v-btn>
            </v-list-item-action>
          </v-list-item>
          <v-divider/>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title class="title font-weight-bold">{{item.title}}</v-list-item-title>
              <v-list-item-subtitle class="subtitle-1">{{new Date(item.createdDate).toLocaleString()}}</v-list-item-subtitle>
              <v-list-item-content class="body-1">{{item.text}}</v-list-item-content>
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
import axios from 'axios'

export default {
  data: () => ({
    isLoaded: false,
    item: {}
  }),
  methods: {
    async fetchSaleItem() {
      try {     
        let response = await axios({
          method: 'GET',
          url: 'http://localhost:8084/api/v1/saleitem/' + this.$store.state.routerParams['/item-detail'].id, 
        })

        if (response.status == 200) {
          this.item = response.data
          this.isLoaded = true 
          console.log(this.item)
          console.log(response.data)
        } 
      } catch(error) {
        if (error.response) {
          alert(error.response.data.error);
          console.log(error.response.data.message);
          if (error.response.status == 401) {
            this.$router.push('/login')
          }
        } else if (error.request) {
          console.log(error.request);
        } else {
          console.log("Error", error.message);
        }
      }
    },
    async startDeal(item) {
      if (item.ownerId == this.$store.state.userId) {
        alert('You cannot buy your items!')
        return
      }

      try {     
        let response = await axios({
          method: 'POST',
          url: 'http://localhost:8084/api/v1/chatting',
          headers: {
            'Content-Type':'application/json'
          },
          data: {
            title: item.title,
            itemId: item.id,
            sellerId: item.ownerId,
            customerId: this.$store.state.userId
          }
        })

        if (response.status == 201) {
          this.$store.commit('setRouterParams', {path: '/chatting-room', value: response.data})
          this.$router.push('/chatting-room')
        } 
      } catch(error) {
        if (error.response) {
          if (error.response.status == 401) {
            this.$router.push('/login')
          } else if (error.response.status == 409) {
            //alert("The chatting room has been already created.")
            await this.enterChattingRoom(item)
          } else {
            alert(error.response.data.error);
            console.log(error.response.data.message);
          }
        } else if (error.request) {
          console.log(error.request);
        } else {
          console.log("Error", error.message);
        }
      }
    },
    async enterChattingRoom(item) {
      try {     
        let response = await axios({
          method: 'GET',
          url: 'http://localhost:8084/api/v1/chatting/' + item.id + '/' + item.ownerId + '/' + this.$store.state.userId
        })

        if (response.status == 200) {
          this.$store.commit('setRouterParams', {path: '/chatting-room', value: response.data})
          this.$router.push('/chatting-room')
        } 
      } catch(error) {
        if (error.response) {
          if (error.response.status == 401) {
            this.$router.push('/login')
          } else {
            alert(error.response.data.error);
            console.log(error.response.data.message);
          }
        } else if (error.request) {
          console.log(error.request);
        } else {
          console.log("Error", error.message);
        }
      }
    },
  },
  mounted() {
    this.$store.commit("setShowBottomNavigation", true)
    this.$store.commit("setSelectedBottomNavigationItem", 'home')
    axios.defaults.headers.common['Authorization'] = this.$store.state.authorization
    this.fetchSaleItem()
  },
};
</script>
