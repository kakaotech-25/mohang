#!/bin/bash

# 태그가 none인 도커 이미지를 찾고 삭제하는 명령어
sudo docker images --filter "dangling=true" -q | while read image_id; do
    echo "Deleting image ID: $image_id"
    sudo docker rmi -f "$image_id"
done

