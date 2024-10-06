#!/bin/bash

set -e
set -o pipefail

# 이미지 정보
NGINX_IMAGE="leovim5072/moheng-nginx:latest"
BACKEND_IMAGE="leovim5072/moheng-backend:latest"
AI_IMAGE="leovim5072/moheng-ai:latest"

# 빌더 이름 설정
BUILDER_NAME="multiarch_builder"

# 기존 빌더가 존재하는지 확인
if ! docker buildx inspect "$BUILDER_NAME" >/dev/null 2>&1; then
    echo "빌더 생성 중..."
    docker buildx create --name "$BUILDER_NAME" --use
else
    echo "기존 빌더 사용 중..."
    docker buildx use "$BUILDER_NAME"
fi

# Docker 로그인 확인
if ! docker info | grep -q 'Username:'; then
    echo "Docker에 로그인되어 있지 않습니다. 로그인하세요."
    exit 1
fi

# 이미지 빌드 및 푸시 함수 정의
build_and_push() {
    local IMAGE_NAME=$1
    local DOCKERFILE=$2
    local CONTEXT=$3

    echo "$IMAGE_NAME 이미지 빌드 중..."
    if docker buildx build --platform linux/amd64 -t "$IMAGE_NAME" -f "$DOCKERFILE" "$CONTEXT" --push; then
        echo "$IMAGE_NAME 이미지 빌드 및 푸시 완료."
    else
        echo "$IMAGE_NAME 이미지 빌드 실패."
        exit 1
    fi
}

# Nginx 이미지 빌드 및 푸시
build_and_push "$NGINX_IMAGE" "nginx/Dockerfile.prod" "./"

# Backend 이미지 빌드 및 푸시
build_and_push "$BACKEND_IMAGE" "backend/Dockerfile.prod" "backend/"

# AI 이미지 빌드 및 푸시
build_and_push "$AI_IMAGE" "ai/Dockerfile" "ai/"

# Docker Hub 로그아웃
docker logout

echo "모든 작업이 완료되었습니다."