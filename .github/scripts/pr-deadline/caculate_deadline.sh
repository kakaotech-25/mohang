#!/bin/bash

# 입력으로 받은 날짜 수
days_to_add="${1}"

# 현재 날짜와 시간을 UTC로 가져옴
current_time=$(date -u +"%Y-%m-%d %H:%M:%S")
echo "Current time (UTC): $current_time"

# 마감 날짜 계산 (UTC)
if [ "$days_to_add" -eq 0 ]; then
    # days_to_add 가 0인 경우 오늘 자정
    deadline_utc=$(date -u -d "tomorrow" +"%Y-%m-%d 00:00:00")
else
    # days_to_add 가 0이 아닌 경우 마감 날짜 계산 (UTC)
    deadline_utc=$(date -u -d "$days_to_add day" +"%Y-%m-%d %H:%M:%S")
fi

# 마감 날짜를 한국 표준시(KST)로 변환하여 출력 (오전/오후 포함)
deadline_kst=$(TZ=Asia/Seoul date -d "$deadline_utc" +"%m월 %d일 %p %I시 %M분")
echo "Deadline (KST): $deadline_kst"

# 결과 출력 & deadline 변수 재정의
echo "deadline=$deadline_kst" >> $GITHUB_OUTPUT