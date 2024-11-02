def containerImages = []

pipeline {
    agent any;

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

        stage('Parallel Build') {
            parallel {
                stage('Nginx Docker Build') {
                    steps {
                        script {
                            echo 'Starting Nginx Build...'
                            containerImages.push(buildDockerImage(
                                directory: 'nginx',
                                imageName: 'leovim5072/moheng-nginx:latest',
                                context: '-f Dockerfile.prod ../'
                            ))
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
                            containerImages.push(buildDockerImage(
                                directory: 'backend',
                                imageName: 'leovim5072/moheng-backend:latest',
                                context: '-f Dockerfile.prod .'
                            ))
                            echo 'Backend Build Completed!'
                        }
                    }
                    post {
                        failure {
                            sendErrorNotification('Backend Build')
                        }
                    }
                }

                stage('AI Docker Build') {
                    steps {
                        script {
                            echo 'Starting AI Build...'
                            containerImages.push(buildDockerImage(
                                directory: 'ai',
                                imageName: 'leovim5072/moheng-ai:latest',
                                context: '-f Dockerfile .'
                            ))
                            echo 'AI Build Completed!'
                        }
                    }
                    post {
                        failure {
                            sendErrorNotification('AI Build')
                        }
                    }
                }
            }
        }

        stage('Upload Container Registry') {
            steps {
                script {
                    echo 'Uploading images to container registry...'
                    pushContainerRegistry(images: containerImages, targetBranch: 'develop')
                    echo 'Upload Completed!'
                }
            }
            post {
                failure {
                    sendErrorNotification('Upload Container Registry')
                }
            }
        }
    }

    post {
        always {
            script {
                echo "Pipeline Completed!"
                def buildStatus = currentBuild.result ?: 'SUCCESS'
                def durationMinutes = (currentBuild.duration / 60000).intValue()
                def durationSeconds = ((currentBuild.duration % 60000) / 1000).intValue()
                def duration = "${durationMinutes}m ${durationSeconds}s"
                sendDiscordNotification(buildStatus, duration)
            }
        }
    }
}

def buildDockerImage(Map params) {
    def directory = params.directory
    def imageName = params.imageName
    def context = params.context
    
    dir(directory) {
        def image = docker.build(imageName, context)
        return [image: image, imageName: imageName]
    }
}

def pushContainerRegistry(Map params) {
    def images = params.images
    def targetBranch = params.targetBranch

    if (env.BRANCH_NAME == targetBranch) {
        images.each { imageData ->
            def image = imageData.image
            def imageName = imageData.imageName
            echo "Pushing image ${imageName} to container registry..."
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