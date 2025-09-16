import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store';
import axios from 'axios';

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

function isAccessTokenExpired(authorization) {
  if (!authorization) return true; // If no authorization header is provided, treat as expired

  try {
    const token = authorization.split(' ')[1]; // Extract the token from the "Bearer <token>" format

    // JWT consists of three parts: header.payload.signature
    const base64Payload = token.split('.')[1]; // Get the payload part (middle section)
    const decodedPayload = JSON.parse(atob(base64Payload)); // Decode from Base64 and parse JSON

    const now = Math.floor(Date.now() / 1000); // Get current time in seconds
    return decodedPayload.exp < now; // Return true if token is expired
  } catch (e) {
    console.error('Token parsing error:', e);
    return true; // If parsing fails, treat as expired
  }
}


router.beforeEach(async (to, from, next) => {
  // Define pages that do not require authentication
  const publicPages = ['/login', '/signup'];
  // Determine if the target route requires authentication
  const authRequired = !publicPages.includes(to.path);

  // Get the current authorization token from the store
  const authorization = store.state.authorization;
  //console.debug('authorization => ', authorization);
  // If authentication is required but no token is present
  if (authRequired && isAccessTokenExpired(authorization)) {
    try {
       let response = null;
      try {
        // Request CSRF token from the server with credentials
        response = await axios.get('/api/v1/auth/csrf', { withCredentials: true });
      } catch (csrfErr) {
        // Ignore CSRF token request errors, optionally log them
        throw new Error('Failed to request CSRF token:', csrfErr);
      }

      // 2. Read the XSRF-TOKEN from cookies
      //const csrfToken = getCookieValue('XSRF-TOKEN');
      const csrfHeaderName = response.data.headerName;
      const csrfToken = response.data.token;
      if (!csrfToken) {
        // Throw error if CSRF token is missing in cookies
        throw new Error('CSRF token does not exist in response.');
      }
      console.debug('CSRF HeaderName: ', csrfHeaderName);
      console.debug('CSRF token: ', csrfToken);

      // 3. Send refresh request with CSRF token in headers to get new access token
      const refreshRes = await axios.post(
        '/api/v1/auth/refresh',
        {},
        {
          withCredentials: true,
          headers: {
            [csrfHeaderName]: csrfToken,
          },
        }
      );

      // Save the new access token in the store
      store.commit('setAuthorization', "Bearer " + refreshRes.data.accessToken);
      // Proceed to the next route
      next();
    } catch (err) {
      // On error (e.g., refresh failed), redirect to login page
      next('/login');
    }
  } else {
    // If authentication is not required or token exists, proceed normally
    next();
  }
});

export default router
