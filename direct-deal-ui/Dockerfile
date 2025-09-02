# 1 Stage: Build (Node.js)
FROM node:20 AS build
WORKDIR /app
COPY ./package*.json ./
ENV NODE_OPTIONS=--openssl-legacy-provider
RUN npm install
COPY . .
RUN npm run build

# 2 Stage: Deploy (Nginx)
FROM nginx:1.25
LABEL image.authors="Jeon, Cheol"
COPY --from=build /app/dist /usr/share/nginx/html
RUN rm -f /etc/nginx/conf.d/default.conf
ADD directdeal.conf /etc/nginx/conf.d
EXPOSE 9000