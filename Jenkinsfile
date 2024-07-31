pipeline {
    agent any

    tools {
        nodejs 'MyNode'
        gradle 'MyGradle'
        jdk 'JDK_22'
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
                        sh 'npm install'
                        
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
                        sh 'chmod +x gradlew'
                        sh './gradlew build'
                    }

                    echo 'Backend Build Completed!'
                }
            }
        }
    }
}