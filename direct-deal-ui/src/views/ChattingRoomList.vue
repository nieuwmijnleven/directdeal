<template>
  <v-container fluid justify-center v-if="isLoaded">
    <v-card flat class="mx-auto">
      <v-list two-line v-for="chattingRoom in chattingRoomList" :key="chattingRoom.id" class="my-0 py-0">
          <v-list-item @click="selectChattingRoom(chattingRoom.id)">
            <v-list-item-avatar class="green white--text" >
              {{(userId == chattingRoom.sellerId) ? chattingRoom.sellerId[0] : chattingRoom.customerId[0]}}
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title>{{chattingRoom.title}}</v-list-item-title>
              <v-list-item-subtitle v-if="chattingRoom.messages.length > 0">{{chattingRoom.messages[chattingRoom.messages.length-1].text}}</v-list-item-subtitle>
            </v-list-item-content>
            <v-list-item-action v-if="(chattingRoom.messages.length - readMessageCount(chattingRoom.id)) > 0">                  
              <div class="px-2 py-0 orange white--text rounded-circle d-inline-block">
                {{(chattingRoom.messages.length - readMessageCount(chattingRoom.id))}}
              </div>
            </v-list-item-action>
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
    chattingRoomList: []
  }),
  computed: {
    userId() {
      return this.$store.state.userId
    }
  },
  methods: {
    readMessageCount(chattingRoomId) {
      let count = this.$store.state.readMessageCountMap[chattingRoomId]
      if (!count) count = 0
      return count
    },
    async fetchChattingRoomList() {
      this.isLoaded = false
      try {     
        let response = await axios({
          method: 'GET',
          url: '/api/v1/chatting/list', 
        })

        if (response.status == 200) {
          this.chattingRoomList = response.data
          this.isLoaded = true
        } else {
          alert("An incorrect response code is returned: " + response.status);
        }
       
      } catch(error) {
        if (error.response) {
          //alert(error.response.data.error);
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
    selectChattingRoom(chattingRoomId) {
      console.log("chatting room id => " + chattingRoomId)
      this.$store.commit('setRouterParams', {path: '/chatting-room', value: {id: chattingRoomId}})
      this.$router.push('/chatting-room')
    }
  },
  mounted() {
    this.$store.commit("setShowBottomNavigation", true)
    this.$store.commit("setSelectedBottomNavigationItem", 'chat')
    axios.defaults.headers.common['Authorization'] = this.$store.state.authorization
    this.fetchChattingRoomList()
  },
};
</script>
