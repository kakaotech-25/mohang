
# EC2 인스턴스 생성
resource "aws_instance" "moheng_prod" {
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = "t3.small"
  subnet_id                   = module.vpc.public_subnets[0]
  vpc_security_group_ids      = [aws_security_group.web_sg.id]
  associate_public_ip_address = true

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

# gp3 EBS 볼륨 생성
resource "aws_ebs_volume" "moheng_prod_volume" {
  availability_zone = aws_instance.moheng_prod.availability_zone
  size              = 16
  type              = "gp3"

  tags = {
    Name = "moheng-prod-volume"
  }
}

# EBS 볼륨을 EC2 인스턴스에 연결
resource "aws_volume_attachment" "moheng_prod_volume_attach" {
  device_name = "/dev/sdf" # Linux에서 사용할 장치 이름
  volume_id   = aws_ebs_volume.moheng_prod_volume.id
  instance_id = aws_instance.moheng_prod.id

  depends_on = [aws_ebs_volume.moheng_prod_volume]
}
