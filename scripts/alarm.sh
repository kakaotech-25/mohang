#!/bin/bash

# 매개변수
webhook_url="https://discord.com/api/webhooks/1284775293348745308/aloYzx2WezgLW9ogAFstoxuxPCRi4AUv_7D7Mk3fQ4K7s2KgTMyi460dtJQgJx9jxAW2"

# 메시지 생성
message="모행 애플리케이션이 재배포 되었습니다."

echo -e "${message}"

# Discord 웹훅으로 메시지 전송

curl -H "Content-Type: application/json" \
     -d "{\"content\": \"${message}\"}" \
     "${webhook_url}"
