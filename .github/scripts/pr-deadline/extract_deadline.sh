#!/bin/bash
label_name="${1}"

if [[ "$label_name" == D-* ]]; then
  deadline=${label_name:2}
  echo "마감 라벨을 설정했습니다. 마감일: $deadline"
  echo "deadline=$deadline" >> $GITHUB_OUTPUT
else
  echo "마감 라벨을 설정하지 않았습니다."
  exit 1
fi