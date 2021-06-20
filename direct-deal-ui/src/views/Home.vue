<template>
  <v-container fluid justify-center>
    <v-card flat class="mx-auto">
      <v-list two-line v-for="item in items" :key="item.id" class="my-0 py-0">
          <v-list-item @click="selectItem(item.id)">
            <v-list-item-avatar size="100" class="rounded" >
              <v-img :src="'http://localhost:8084/api/v1/image/' + item.mainImage"></v-img>
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title>{{item.title}}</v-list-item-title>
              <v-list-item-subtitle>{{item.targetPrice}}원</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider></v-divider>
      </v-list>
    </v-card>
  </v-container>
</template>

<script>
import axios from 'axios'

export default {
  data: () => ({
    items: [
      {
        "id": "7ed0820f-42ce-4e74-9cee-5f3ec25a841f",
        "title": "1",
        "category": "gems",
        "targetPrice": 2,
        "discountable": false,
        "mainImage": "e11c203d-39d6-439e-9ed4-cbd507ed5ba3.jpg",
        "status": "SALE",
        "createdDate": "2021-06-20T01:33:04.271Z",
      }
    ]
  }),
  methods: {
    async fetchSaleList() {
      try {     
        let response = await axios({
          method: 'GET',
          url: 'http://localhost:8084/api/v1/salelist?page=0&size=10&sort=createdDate,desc', 
        })

        if (response.status == 200) {
          //this.$router.push("/home");
          //alert("Success")
          // for (const item of response.data) {
          //   console.log(item)
          //   item.mainImage = 'http://localhost:8084/api/v1/image/' + item.mainImage
          // }
          this.items = response.data
        } else {
          alert("An incorrect response code is returned: " + response.status);
        }
       
      } catch(error) {
        if (error.response) {
          alert(error.response.data.error);
          console.log(error.response.data.message);
        } else if (error.request) {
          console.log(error.request);
        } else {
          console.log("Error", error.message);
        }
      }
    },
    selectItem(itemId) {
      console.log("select item => " + itemId)
      //this.$router.params = {id: itemId}
      this.$store.commit('setRouterParams', {id: itemId})
      this.$router.push('/item-detail')
      //this.$router.push({path: '/item-detail', params:{id: itemId}})
    }
  },
  mounted() {
    this.$store.commit("setShowBottomNavigation", true)
    this.$store.commit("setSelectedBottomNavigationItem", 'home')
    axios.defaults.headers.common['Authorization'] = this.$store.state.authorization
    this.fetchSaleList()
  },
};
</script>
