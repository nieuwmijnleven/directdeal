// src/axios.js
import axios from 'axios';
import store from './store';
import router from './router';

const api = axios.create({
  // baseURL is commented out to allow relative URL requests
  // baseURL: 'https://directdeal.nl',

  // withCredentials defaults to false
  // Cookies are automatically sent with same-origin requests
  // For cross-origin requests where cookies (like refreshToken) are needed,
  // you must set withCredentials: true explicitly
  // withCredentials: true
});


// 요청 인터셉터 - accessToken이 있다면 헤더에 추가
api.interceptors.request.use(
  config => {
    const token = store.state.authorization;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// 응답 인터셉터 - accessToken이 만료되었을 경우 처리
let isRefreshing = false;
let refreshSubscribers = [];

function onRefreshed(token) {
  refreshSubscribers.forEach(callback => callback(token));
  refreshSubscribers = [];
}

function addRefreshSubscriber(callback) {
  refreshSubscribers.push(callback);
}

api.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    if (
      error.response &&
      error.response.status === 401 &&
      !originalRequest._retry
    ) {
      // accessToken 만료된 경우
      originalRequest._retry = true;

      if (isRefreshing) {
        return new Promise(resolve => {
          addRefreshSubscriber(token => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            resolve(api(originalRequest));
          });
        });
      }

      isRefreshing = true;

      try {
        const res = await axios.post(
          '/auth/refresh',
          {},
          {}
        );

        const newToken = res.data.accessToken;
        store.commit('setAuthorization', newToken);
        onRefreshed(newToken);
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        // refreshToken도 만료된 경우
        router.push('/login');
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default api;