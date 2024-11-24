
# EC2 인스턴스 생성
resource "aws_instance" "moheng_prod" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = "t3.small"
  subnet_id                   = module.vpc.public_subnets[0]
  vpc_security_group_ids      = [aws_security_group.web_sg.id]
  associate_public_ip_address = true

  root_block_device {
    volume_size           = 24              # 루트 볼륨 크기 (GiB)
    volume_type           = "gp2"           # 볼륨 타입 (기본값: gp2)
    delete_on_termination = true            # 인스턴스 종료 시 볼륨 삭제 여부 (기본값: true)
    encrypted             = true            # 볼륨 암호화 여부 (선택 사항)
  }

  tags = {
    Name = "moheng-prod"
  }
}

# 최신 Ubuntu AMI 데이터 소스
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"] # Canonical

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}


resource "aws_eip" "moheng_prod_eip" {
  tags = {
    Name = "moheng-prod-eip"
  }

  # EIP가 의도치 않게 삭제되지 않도록 보호
  lifecycle {
    prevent_destroy = true
  }
}

# 탄력적 IP를 EC2 인스턴스에 연결
resource "aws_eip_association" "moheng_prod_eip_assoc" {
  instance_id   = aws_instance.moheng_prod.id
  allocation_id = aws_eip.moheng_prod_eip.id
}

# EC2 인스턴스의 공개 IP 출력
output "ec2_public_ip" {
  value = aws_eip.moheng_prod_eip.public_ip
}
