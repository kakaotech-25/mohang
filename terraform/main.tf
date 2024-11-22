# AWS 프로바이더 설정
provider "aws" {
  region = "ap-northeast-2" # 서울 리전
}

# 테라폼 클라우드 연동
terraform {
  cloud {

    organization = "kakaotech-harmony"

    workspaces {
      name = "moheng"
    }
  }
}