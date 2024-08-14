pipeline {
    agent any

    tools {
        nodejs 'MyNode'
        gradle 'MyGradle'
        jdk 'JDK_22'
    }

    environment {
        JAVA_HOME = "${tool 'JDK_22'}"
        PATH = "${env.PATH}:${env.JAVA_HOME}/bin"
    }

    def errorLog = ''
    def failedStage = ''

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo 'Checking out code...'
                }
            }
        }
        stage('Frontend Build') {
            steps {
                script {
                    echo 'Starting Frontend Build...'
                    
                    dir('frontend') {
                        echo 'Installing Frontend Dependencies...'
                        sh 'npm install --legacy-peer-deps'

                        echo 'Building Frontend...'
                        sh 'npm run build'
                    }
                    
                    echo 'Frontend Build Completed!'
                }
            }
        }
        stage('Backend Build') {
            steps {
                script {
                    echo 'Starting Backend Build...'
                    
                    dir('backend') {
                        echo 'Installing Backend Dependencies...'
                        // sh 'chmod +x gradlew'
                        // sh './gradlew build'

                        // Test: 아래 명령어는 존재하지 않는 파일을 실행하여 오류를 발생시킵니다.
                        sh './nonexistent-file'
                        //-----------------------
                    }

                    echo 'Backend Build Completed!'
                }
            }
        }
    }

    post {
        always {
            script {
                // 실패한 경우, 실패한 스테이지 이름과 로그를 수집
                if (currentBuild.result == 'FAILURE') {
                    failedStage = env.STAGE_NAME
                    errorLog = sh(script: 'tail -n 50 $WORKSPACE/jenkins.log', returnStdout: true).trim()
                }
            }
        }
        success {
            script {
                notifyDiscord("성공", "")
            }
        }
        failure {
            script {
                notifyDiscord("실패", errorLog)
            }
        }
    }
}

// Discord 알림 함수
def notifyDiscord(String status, String log) {
    withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD')]) {
        discordSend description: """
        제목 : ${currentBuild.displayName}
        결과 : ${currentBuild.result} - ${status}
        실패한 단계 : ${status == '실패' ? failedStage : 'N/A'}
        에러 로그 : 
        ${log}
        실행 시간 : ${currentBuild.duration / 1000}s
        """,
        link: env.BUILD_URL, result: currentBuild.currentResult, 
        title: "${env.JOB_NAME} : ${currentBuild.displayName} ${status}", 
        webhookURL: "$DISCORD"
    }
}