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

    stages {
        stage ('Checkout') {
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
                        // echo 'Installing Backend Dependencies...'
                        // sh 'chmod +x gradlew'
                        // sh './gradlew build'

                        //test : 없는 파일 명령어 실행
                        sh './gradlew ajfdksl'
                    }

                    echo 'Backend Build Completed!'
                }
            }
        }
    }

    post {
        success {
            withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD')]) {
                discordSend description: """
                제목 : ${currentBuild.displayName}
                결과 : ${currentBuild.result}
                실행 시간 : ${currentBuild.duration / 1000}s
                """,
                link: env.BUILD_URL, result: currentBuild.currentResult, 
                title: "${env.JOB_NAME} : ${currentBuild.displayName} 성공", 
                webhookURL: "$DISCORD"
            }
        }
        failure {
            script {
                def failedStage = currentBuild.rawBuild.execution.timeInMillis
                def errorMessage = ''

                try {
                    errorMessage = currentBuild.rawBuild.getLog(50).join("\n")
                } catch (Exception e) {
                    errorMessage = 'Failed to retrieve error message.'
                }

                withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD')]) {
                    discordSend description: """
                    제목 : ${currentBuild.displayName}
                    결과 : ${currentBuild.result}
                    실패 단계: ${failedStage}
                    오류 메시지: ${errorMessage}
                    실행 시간 : ${currentBuild.duration / 1000}s
                    """,
                    link: env.BUILD_URL, result: currentBuild.currentResult, 
                    title: "${env.JOB_NAME} : ${currentBuild.displayName} 실패", 
                    webhookURL: "$DISCORD"
                }
            }
        }
    }
}