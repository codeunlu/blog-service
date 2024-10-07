#!/bin/bash

docker run -d --name postgres \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=qwerty123 \
  -e POSTGRES_DB=blogdb \
  -p 6432:5432 \
  postgres:latest

echo "Postgres container started."
sleep 2

docker run -d --name redis -p 6380:6379 redis redis-server --requirepass "qwerty123"
echo "Redis container started."
sleep 2

