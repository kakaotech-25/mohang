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
                        echo 'Installing Backend Dependencies...'
                        // sh 'chmod +x gradlew'
                        // sh './gradlew build'

                        //test: 아래 명령어는 존재하지 않는 파일을 실행하여 오류를 발생시킵니다.
                        sh './nonexistent-file'
                        //-----------------------
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
                def errorLog = currentBuild.rawBuild.getLog(50).join("\n")

                withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD')]) {
                    discordSend description: """
                    제목 : ${currentBuild.displayName}
                    결과 : ${currentBuild.result}
                    에러 로그 : 
                    ${errorLog}
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
