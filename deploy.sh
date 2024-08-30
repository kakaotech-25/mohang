#!/bin/bash

# 도커 컴포즈 파일 경로
COMPOSE_FILE="compose.prod.yaml"

# 1. 현재 작동 중인 컨테이너를 멈추고 제거
echo "Stopping and removing current containers..."
docker-compose -f $COMPOSE_FILE down

# 2. 최신 이미지를 풀링
echo "Pulling the latest images..."
docker-compose -f $COMPOSE_FILE pull

# 3. 컨테이너를 백그라운드에서 다시 시작
echo "Starting containers..."
docker-compose -f $COMPOSE_FILE up -d

echo "Containers are up and running!"