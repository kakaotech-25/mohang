FROM python:3.11-slim AS build

WORKDIR /app

# Poetry 쉘 가상화 옵션 비활성화 (오버헤드 최소화)
RUN pip install --upgrade poetry && \
    poetry config virtualenvs.create false 

# 프로젝트의 Poetry 파일을 복사 (pyproject.toml 및 poetry.lock)
COPY pyproject.toml poetry.lock ./

# devDependencies를 제거한 프로덕션 의존성 설치 
RUN poetry install --only main \
    && rm -rf /root/.cache/pypoetry

# --------------------------------------------------------------------

FROM python:3.11-slim

WORKDIR /app

# 버전 / 경로 명시
ARG VERSION=3.11
ENV PYTHONPATH="/usr/local/lib/python${VERSION}/site-packages"
ARG BIN="/usr/local/bin"

# 빌드 단계에서 설치한 의존성을 복사
COPY --from=build ${PYTHONPATH} ${PYTHONPATH}
COPY --from=build ${BIN} ${BIN}

# 프로젝트 소스 코드 복사
COPY . .

# 유저 및 그룹 생성 & 소스 코드 및 작업 디렉토리 권한 변경 (유저에게 권한 부여)
RUN groupadd -r appgroup && useradd -r -g appgroup appuser && \
    chown -R appuser:appgroup /app

# 유저 권한으로 전환
USER appuser

# 주의 --host 구문 : poetry도 가상환경이라 포트포워딩 안하면 요청이 안 들어옵니다. 
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
