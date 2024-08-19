# 빌드 스테이지
FROM openjdk:22-slim AS build

WORKDIR /app

# gradle 설정파일 복사
COPY gradlew gradle build.gradle settings.gradle /app/
COPY gradle/wrapper /app/gradle/wrapper

# 실행권한 부여 & build.gradle 의존성 다운로드
RUN chmod +x ./gradlew \
    && ./gradlew dependencies

# 소스코드 복사
COPY src /app/src

# 소스코드 빌드
RUN ./gradlew build

# 최종 이미지
FROM openjdk:22-slim

WORKDIR /app

# 빌드된 결과물 복사
COPY --from=build /app/build/libs /app/libs

# 최소권한 설정
RUN groupadd -r appgroup && useradd -r -g appgroup appuser \
    && mkdir -p /home/appuser/.gradle \
    && chown -R appuser:appgroup /app /home/appuser/.gradle

USER appuser

# CMD ["tail", "-f"]
# 추후 jar파일의 이름이 변경되어도 첫번째 행을 실행시키도록 함
CMD ["sh", "-c", "java -jar $(ls build/libs/*SNAPSHOT.jar | head -n 1)"]
