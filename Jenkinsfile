pipeline {
    agent {
        docker {
            image 'docker:latest'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        IMAGE_STORAGE_CREDENTIAL = 'dockerhub-access-token'
        IMAGE_STORAGE = 'https://registry.hub.docker.com'
    }

    stages {
        stage('Docker Login Test') {
            steps {
                script {
                    echo 'Testing Docker Login...'
                    docker.withRegistry(IMAGE_STORAGE, IMAGE_STORAGE_CREDENTIAL) {
                        echo 'Docker login successful!'
                    }
                }
            }
            post {
                failure {
                    script {
                        echo 'Docker login failed. Exiting...'
                        currentBuild.result = 'FAILURE'
                        error('Stopping the pipeline due to Docker login failure.')
                    }
                }
            }
        }

        stage('Checkout') {
            steps {
                script {
                    echo 'Checking out code...'
                }
            }
            post {
                failure {
                    sendErrorNotification('Checkout')
                }
            }
        }

        stage('Parallel Build'){
            parallel{
                stage('Nginx Docker Build') {
                    steps {
                        script {
                            echo 'Starting Nginx Build...'
                            buildAndPushDockerImage(
                                directory: 'nginx',
                                imageName: 'leovim5072/moheng-nginx:latest',
                                targetBranch: 'develop',
                                context: '-f Dockerfile.prod ../'
                            )
                            // dir('nginx') {
                            //     docker.withRegistry(IMAGE_STORAGE, IMAGE_STORAGE_CREDENTIAL) {
                            //         def nginxImage = docker.build("leovim5072/moheng-nginx:latest", "-f Dockerfile.prod ../")
                            //         nginxImage.push("latest")
                            //     }
                            // }
                            echo 'Nginx Build Completed!'
                        }
                    }
                    post {
                        failure {
                            sendErrorNotification('Nginx Build')
                        }
                    }
                }

                stage('Backend Docker Build') {
                    steps {
                        script {
                            echo 'Starting Backend Build...'
                            buildAndPushDockerImage(
                                directory: 'backend',
                                imageName: 'leovim5072/moheng-backend:latest',
                                targetBranch: 'develop',
                                context: '-f Dockerfile.prod .'
                            )
                            // dir('backend') {
                            //     docker.withRegistry(IMAGE_STORAGE, IMAGE_STORAGE_CREDENTIAL) {
                            //         def backendImage = docker.build("leovim5072/moheng-backend:latest", "-f Dockerfile.prod .")
                            //         backendImage.push("latest")
                            //     }
                            // }
                            echo 'Backend Build Completed!'
                        }
                    }
                    post {
                        failure {
                            sendErrorNotification('Backend Build')
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                def buildStatus = currentBuild.result ?: 'SUCCESS'
                def duration = "${currentBuild.duration / 60000}m ${currentBuild.duration % 60000 / 1000}s"
                sendDiscordNotification(buildStatus, duration)
            }
        }
    }
}

def buildAndPushDockerImage(Map params) {
    def directory = params.directory
    def imageName = params.imageName
    def targetBranch = params.targetBranch
    def context = params.context
    
    dir(directory) {
        def image = docker.build(imageName, context)
        if (env.BRANCH_NAME == targetBranch) {
            docker.withRegistry(IMAGE_STORAGE, IMAGE_STORAGE_CREDENTIAL) {
                image.push("latest")
            }
        }
    }
}
        
def sendDiscordNotification(buildStatus, duration) {
    withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD')]) {
        discordSend description: """
        제목 : ${currentBuild.displayName}
        결과 : ${buildStatus}
        실행 시간 : ${duration}
        """,
        link: env.BUILD_URL, result: buildStatus, 
        title: "${env.JOB_NAME} : ${currentBuild.displayName} ${buildStatus}", 
        webhookURL: "$DISCORD"
    }
}
