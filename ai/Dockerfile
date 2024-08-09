FROM python:3.11-slim

# 작업 디렉터리 설정
WORKDIR /app

RUN pip install -U poetry

# Poetry를 이용하여 의존성 설치
# RUN poetry config virtualenvs.create false 

# Poetry 설치
RUN curl -sSL https://install.python-poetry.org | python3 -

# 환경 변수 설정 (Poetry 설정을 위해)
ENV PATH="/root/.local/bin:$PATH"

# 프로젝트의 Poetry 파일을 복사 (pyproject.toml 및 poetry.lock)
COPY pyproject.toml poetry.lock ./

# 의존성 설치
RUN poetry install --no-root

# 로컬에 있는 소스코드 컨테이너로 복사
COPY . /app

CMD ["poetry", "run", "uvicorn", "main:app"]