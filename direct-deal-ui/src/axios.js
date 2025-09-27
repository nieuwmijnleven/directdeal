// src/axios.js
import axios from 'axios';
import store from './store';
import router from './router';

function isAccessTokenExpired(token) {
  if (!token) return true; // If no authorization header is provided, treat as expired

  try {
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

// Base64 URL-safe 인코딩 함수
function base64UrlEncode(arrayBuffer) {
  const base64 = btoa(String.fromCharCode(...new Uint8Array(arrayBuffer)));
  return base64.replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
}

// XOR 연산 함수 (Uint8Array 두 개를 받아 XOR 결과 반환)
function xorBytes(a, b) {
  if (a.length !== b.length) {
    throw new Error('두 배열 길이가 같아야 합니다');
  }
  const result = new Uint8Array(a.length);
  for (let i = 0; i < a.length; i++) {
    result[i] = a[i] ^ b[i];
  }
  return result;
}

// 쿠키에서 CSRF 토큰 읽기
function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
}

function createEncryptedCsrfToken() {
  // 쿠키에서 원본 CSRF 토큰 읽기
  const tokenStr = getCookie('XSRF-TOKEN');
  console.warn('XSRF-TOKEN: ', tokenStr);
  if (!tokenStr) {
    throw new Error('There is no CSRF token in the cookie.');
  }

  // UTF-8 인코딩 (문자열 → 바이트 배열)
  const tokenBytes = new TextEncoder().encode(tokenStr);

  // 같은 길이의 랜덤 바이트 생성
  const randomBytes = crypto.getRandomValues(new Uint8Array(tokenBytes.length));

  // randomBytes XOR tokenBytes
  const xoredBytes = xorBytes(randomBytes, tokenBytes);

  // randomBytes + xoredBytes 합치기
  const combined = new Uint8Array(randomBytes.length + xoredBytes.length);
  combined.set(randomBytes, 0);
  combined.set(xoredBytes, randomBytes.length);

  // Base64 URL-safe 인코딩
  return base64UrlEncode(combined.buffer);
}

const api = axios.create({
  // baseURL is commented out to allow relative URL requests
  // baseURL: 'https://directdeal.nl',

  // withCredentials defaults to false
  // Cookies are automatically sent with same-origin requests
  // For cross-origin requests where cookies (like refreshToken) are needed,
  // you must set withCredentials: true explicitly
    withCredentials: true
});

// 요청 인터셉터 - accessToken이 있다면 헤더에 추가
api.interceptors.request.use(
  config => {
    const token = store.state.accessToken;
    if (token && !isAccessTokenExpired(token)) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    //console.warn("config.headers: ", config.headers);

    if (config.headers['X-XSRF-TOKEN']) {
        config.headers['X-XSRF-TOKEN'] = createEncryptedCsrfToken(config.headers['X-XSRF-TOKEN']);
        console.warn('config.headers[\'X-XSRF-TOKEN\']', config.headers['X-XSRF-TOKEN']);
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
        await axios.get('/api/v1/auth/csrf', { withCredentials: true });
//        const csrfHeaderName = csrfRes.data.headerName;
//        const csrfToken = csrfRes.data.token;
//        if (!csrfToken) {
//            // Throw error if CSRF token is missing in response body.
//            throw new Error('CSRF token does not exist in response.');
//        }
//        console.debug('CSRF HeaderName: ', csrfHeaderName);
//        console.debug('CSRF token: ', csrfToken);

        const encryptedCsrfToken = createEncryptedCsrfToken();
        console.warn('encryptedCsrfToken: ', encryptedCsrfToken);

        const interceptor = axios.interceptors.request.use(
          config => {
            config.xsrfCookieName = '';     // 쿠키 이름 비활성화
            config.xsrfHeaderName = '';     // 헤더 이름 비활성화
            config.headers['X-XSRF-TOKEN'] = encryptedCsrfToken;
            return config;
          },
          error => Promise.reject(error)
        );

        const refreshRes = await axios.post(
          '/api/v1/auth/refresh',
          {},
          {
            withCredentials: true,
            /*headers: {
              'X-XSRF-TOKEN': encryptedCsrfToken,
            },*/
          }
        );

        axios.interceptors.request.eject(interceptor);

        const newToken = refreshRes.data.accessToken;
        store.commit('setAccessToken', newToken);
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