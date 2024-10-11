FROM mysql:8.2.0

ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=band_hub_db
ENV MYSQL_USER=developer
ENV MYSQL_PASSWORD=developer

COPY ./src/main/resources/db/migration ./

EXPOSE 3306