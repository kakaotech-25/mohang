#!/bin/bash

# 입력으로 받은 날짜 수
days_to_add="${1}"

# 마감 날짜 계산 (KST)
if [ "$days_to_add" -eq 0 ]; then
    # days_to_add 가 0인 경우 오늘 KST 자정의 날짜를 UTC로 변환
    deadline_kst=$(TZ='Asia/Seoul' date -d 'tomorrow 00:00' +"%Y-%m-%d %H:%M:%S")
else
    # days_to_add 가 0이 아닌 경우, 해당 일 수를 추가한 KST 자정의 날짜를 UTC로 변환
    deadline_kst=$(TZ='Asia/Seoul' date -d "$days_to_add days" +"%Y-%m-%d %H:%M:%S")
fi

echo "Deadline (KST): $deadline_kst"


# 결과 출력 & deadline 변수 재정의
echo "deadline=$deadline_kst" >> $GITHUB_OUTPUT