#!/bin/bash

DISCORD_WEBHOOK=$1
GITHUB_WORKFLOW=$2
GITHUB_ACTOR=$3
GITHUB_REF_NAME=$4
GITHUB_SHA=$5
GITHUB_REPOSITORY=$6
BUILD_STATUS=$7

# 빌드 상태에 따라 색상과 메시지 설정
if [ "$BUILD_STATUS" == "success" ]; then
  STATUS_MSG="✅ Build succeeded"
  COLOR=65280  # 녹색
else
  STATUS_MSG="❌ Build failed"
  COLOR=15158332  # 빨간색
fi

curl -H "Content-Type: application/json" \
     -X POST \
     -d '{
           "username": "CI/CD Bot",
           "embeds": [
             {
               "title": "'"${STATUS_MSG}"'",
               "description": "Build triggered by **'"${GITHUB_ACTOR}"'** on branch **'"${GITHUB_REF_NAME}"'**.",
               "color": '"${COLOR}"',
               "fields": [
                 {
                   "name": "🔄 Commit",
                   "value": "[`'"${GITHUB_SHA}"'`](https://github.com/'"${GITHUB_REPOSITORY}"'/commit/'"${GITHUB_SHA}"')",
                   "inline": true
                 },
                 {
                   "name": "📂 Repository",
                   "value": "'"${GITHUB_REPOSITORY}"'",
                   "inline": true
                 },
                 {
                   "name": "👤 Actor",
                   "value": "'"${GITHUB_ACTOR}"'",
                   "inline": true
                 }
               ],
               "footer": {
                 "text": "Build status: '"${BUILD_STATUS}"'"
               }
             }
           ]
         }' \
     "${DISCORD_WEBHOOK}"