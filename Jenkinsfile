pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'master-keycloak:latest'
        IMAGE_TAG = "1.0.2"
        DOCKER_CREDENTIALS = 'dockerhub-credential'

    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/phamminhtan1998/identity_service'
            }
        }
        stage('Build Maven') {
        steps {
         withMaven (
             maven: 'Maven 3.9.9',
              mavenLocalRepo: '/Users/phamminhtan/.m2/repository'
         ){
                        sh 'mvn -U clean install'
                    }
        }

        }
        stage('Build Docker') {
            steps {
                sh 'docker build -t master-keycloak .'
            }
        }
        stage('Login to Docker Hub'){
            steps {
                script {
                    echo 'Login to Docker Hub'
                    docker.withRegistry('https://hub.docker.com', 'dockerhub-credential')
                }
            }
        }
        stage('Push to Registry') {
            steps {
                script {
                echo 'Tag and push docker image'
                sh "docker tag ${DOCKER_IMAGE} phamminhtan/identity_service:${IMAGE_TAG}"
                sh "docker push phamminhtan/identity_service:${IMAGE_TAG}"
                }
            }
        }

    }
}