FROM node:20-bullseye AS build
WORKDIR /app

COPY package.json ./
RUN npm install parse5@6.0.1 --save-exact
COPY . .
RUN npm run build -- --configuration production --project band-hub-frontend

FROM nginx:alpine
COPY --from=build /app/dist/band-hub-frontend /usr/share/nginx/html
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
