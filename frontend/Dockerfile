FROM node:18.20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci --quiet
COPY . .
RUN npm run build -- --configuration=production

FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/pi-street-league /usr/share/nginx/html
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]
