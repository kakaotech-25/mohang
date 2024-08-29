#!/bin/bash

# SSL 디렉토리가 없으면 생성
mkdir -p ../ssl

# OpenSSL 명령어 실행
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout ./nginx/ssl/cert.key -out ./nginx/ssl/cert.pem -subj "/C=KR/ST=Seoul/L=Seoul/O=MyCompany/OU=MyDepartment/CN=www.mywebsite.com/emailAddress=admin@mywebsite.com"

# 명령어의 실행 상태를 확인
if [ $? -eq 0 ]; then
    echo "개인 키, CSR, 인증서 생성 완료."
else
    echo "에러: 인증서 생성에 실패했습니다."
fi