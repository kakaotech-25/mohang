#!/bin/bash

# 매개변수
webhook_url="${1}"
deadline="${2}"

# 메시지 생성
message="마감기간 : ${deadline}"

echo -e "${message}"

# Discord 웹훅으로 메시지 전송

curl -H "Content-Type: application/json" \
     -d "{\"content\": \"${message}\"}" \
     "${webhook_url}"