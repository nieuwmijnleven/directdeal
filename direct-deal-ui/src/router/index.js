import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import SignUp from '../views/SignUp.vue'
import RegisterItem from '../views/RegisterItem.vue'
import ItemDetail from '../views/ItemDetail.vue'
import ChattingRoomList from '../views/ChattingRoomList.vue'
import ChattingRoom from '../views/ChattingRoom.vue'
import TransactionHistory from '../views/TransactionHistory.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/signup',
    name: 'SignUp',
    component: SignUp
  },
  {
    path: '/register-item',
    name: 'RegisterItem',
    component: RegisterItem
  },
  {
    path: '/item-detail',
    name: 'ItemDetail',
    component: ItemDetail
  },
  {
    path: '/chatting-room-list',
    name: 'ChattingRoomList',
    component: ChattingRoomList
  },
  {
    path: '/chatting-room',
    name: 'ChattingRoom',
    component: ChattingRoom
  },
  {
    path: '/transaction-history',
    name: 'TransactionHistory',
    component: TransactionHistory
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
