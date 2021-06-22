<template>
  <v-container class="fill-height">
      <v-row class="fill-height pb-16" align="end">
        <v-col cols="12">
          <div v-for="(message, index) in chattingRoom.messages" :key="index" 
              :class="['d-flex flex-row align-center my-2', message.talkerId == userId ? 'justify-end': null]">
            <span v-if="message.talkerId == userId" class="blue--text mr-3 rounded">{{ message.text }}</span>
            <v-avatar :color="message.talkerId == userId ? 'indigo': 'red'" size="36">
               <span class="white--text">{{ message.talkerId[0] }}</span>
            </v-avatar>
            <span v-if="message.talkerId != userId" class="blue--text ml-3">{{ message.text }}</span>
          </div>
        </v-col>
      </v-row>
      <v-footer fixed>
        <v-container class="mx-0 mt-0 mb-15 pa-0">
          <v-row no-gutters>
            <v-col cols="12">
              <div class="d-flex flex-row align-center">
                <v-text-field v-model="message" placeholder="Type Something" @keypress.enter="send"></v-text-field>
                <v-btn icon class="ml-4" @click="send"><v-icon>mdi-send</v-icon></v-btn>
              </div>
            </v-col>
          </v-row>
        </v-container>
      </v-footer>
    </v-container>
</template>

<script>
import axios from 'axios'

export default {
  data: () => ({
    chattingRoom: {},
    message: null,
    timerId: null
  }),
  computed: {
    userId() {
      return this.$store.state.userId;
    }
  },
  methods: {
    async send() {
      try {     
        let response = await axios({
          method: 'PUT',
          url: 'http://localhost:8084/api/v1/chatting',
          headers: {
            'Content-Type':'application/json'
          },
          data: {
            chattingRoomId: this.chattingRoom.id,
            talkerId: this.$store.state.userId,
            text: this.message
          }
        })

        if (response.status == 201) {
          this.fetchNewMessages()
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

      this.message = null
    },
    async fetchNewMessages() {
      try {     
        let response = await axios({
          method: 'GET',
          url: 'http://localhost:8084/api/v1/chatting/' + this.chattingRoom.id + '/fetch-from/' + this.chattingRoom.messages.length
        })

        if (response.status == 200) {
          this.chattingRoom.messages = this.chattingRoom.messages.concat(response.data)
          console.log(this.chattingRoom.messages)
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
    async fetchChattingRoom() {
      this.chattingRoom = this.$store.state.routerParams['/chatting-room']
      console.log('chattingRoom => ' + JSON.stringify(this.chattingRoom))
     
      try {     
        let response = await axios({
          method: 'GET',
          url: 'http://localhost:8084/api/v1/chatting/' + this.chattingRoom.id
        })

        if (response.status == 200) {
          this.chattingRoom = response.data
          console.log(this.chattingRoom)
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
  },
  mounted() {
    this.$store.commit("setShowBottomNavigation", true)
    this.$store.commit("setSelectedBottomNavigationItem", 'chat')
    axios.defaults.headers.common['Authorization'] = this.$store.state.authorization
    this.fetchChattingRoom()
    this.timerId = setInterval(() => this.fetchNewMessages(), 1000)
  },
  beforeDestroy() {
    clearTimeout(this.timerId)
    this.$store.commit("setReadMessageCountMap", {id:this.chattingRoom.id, count:this.chattingRoom.messages.length})
    console.log("update readMessageCountMap => " + JSON.stringify(this.$store.state.readMessageCountMap))
  }
};
</script>
