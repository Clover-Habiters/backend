#!/bin/bash

IS_DB_RUNNING=$(docker ps | grep -c "db") # db가 실행중인지

if [ "$IS_DB_RUNNING" -eq 0 ]; then # 실행중이 아니라면
  echo "### Starting database ###"
  docker-compose up -d db
else
  echo "db is already running"
fi

echo

IS_REDIS_RUNNING=$(docker ps | grep -c "redis") # redis가 실행중인지

if [ "$IS_REDIS_RUNNING" -eq 0 ]; then # 실행중이 아니라면
  echo "### Starting redis ###"
  docker-compose up -d redis
else
  echo "redis is already running"
fi

IS_GREEN_RUNNING=$(docker ps | grep green) # green이 실행중인지

if [ "$IS_GREEN_RUNNING" -eq 0 ];then # green이 실행중이 아니라면 -> green으로 실행

  echo "### BLUE => GREEN ###"

  echo "1. get green image"
  docker-compose pull green # 이미지 받아서

  echo "2. green container up"
  docker-compose up -d green # 컨테이너 실행

  while [ 1 = 1 ]; do
  echo "3. green health check..."
  sleep 3

  REQUEST=$(curl http://127.0.0.1:8080) # green으로 request
    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
            echo "health check success"
            break ;
            fi
  done;

  echo "4. reload nginx"
  sudo cp /etc/nginx/nginx.green.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "5. blue container down"
  docker-compose stop blue

else # green이 실행중이라면 -> blue로 실행
  echo "### GREEN => BLUE ###"

  echo "1. get blue image"
  docker-compose pull blue

  echo "2. blue container up"
  docker-compose up -d blue

  while [ 1 = 1 ]; do
    echo "3. blue health check..."
    sleep 3
    REQUEST=$(curl http://127.0.0.1:8081) # blue로 request

    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
      echo "health check success"
      break ;
    fi
  done;

  echo "4. reload nginx"
  sudo cp /etc/nginx/nginx.blue.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "5. green container down"
  docker-compose stop green
fi
