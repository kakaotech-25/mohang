pipeline {
    agent any

    tools {
        nodejs 'MyNode'
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
                    echo 'Installing Backend Dependencies...'
                    echo 'Running Backend Tests...'
                    echo 'Building Backend...'
                    echo 'Backend Build Completed!'
                }
            }
        }
    }
}