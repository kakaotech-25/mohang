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

variable "AWS_ACCESS_KEY_ID" {
  description = "AWS Access Key ID"
  type        = string
}

variable "AWS_SECRET_ACCESS_KEY" {
  description = "AWS Secret Access Key"
  type        = string
}