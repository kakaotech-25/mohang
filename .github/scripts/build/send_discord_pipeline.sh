#!/bin/bash

DISCORD_WEBHOOK=$1
GITHUB_WORKFLOW=$2
GITHUB_ACTOR=$3
GITHUB_REF_NAME=$4
GITHUB_SHA=$5
GITHUB_REPOSITORY=$6
BUILD_TIME=$7

curl -H "Content-Type: application/json" \
     -X POST \
     -d '{
           "username": "CI/CD Bot",
           "embeds": [
             {
               "title": "🚀 Build Pipeline Status: '"${GITHUB_WORKFLOW}"'",
               "description": "Build triggered by **'"${GITHUB_ACTOR}"'** on branch **'"${GITHUB_REF_NAME}"'**.",
               "color": 65280,
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
                 },
                 {
                   "name": "⏰ Build Time",
                   "value": "'"${BUILD_TIME}"'",
                   "inline": true
                 }
               ],
               "footer": {
                 "text": "Build completed at '"${BUILD_TIME}"'"
               }
             }
           ]
         }' \
     "${DISCORD_WEBHOOK}"