FROM python:3.11-slim AS build

WORKDIR /app

RUN pip install --upgrade poetry

# Poetry를 가상환경에서 사용하지 않도록 설정 (가상화 오버헤드 최소화)
RUN poetry config virtualenvs.create false 

# 프로젝트의 Poetry 파일을 복사 (pyproject.toml 및 poetry.lock)
COPY pyproject.toml poetry.lock ./

# devDependencies를 제거한 프로덕션 의존성 설치 
RUN poetry install --only main 

########################################

FROM python:3.11-slim

WORKDIR /app

# 파이썬 라이브러리 복사
COPY --from=build /usr/local /usr/local

# 애플리케이션 코드 복사
COPY . .

# 주의 --host 구문 : poetry도 가상환경이라 포트포워딩 안하면 요청이 안 들어옵니다. 
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
