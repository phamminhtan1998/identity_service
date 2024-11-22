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
                withMaven(
                    maven: 'Maven 3.9.9',
                    mavenLocalRepo: '/Users/phamminhtan/.m2/repository'
                ) {
                    sh 'mvn -U clean install'
                }
            }
        }
        stage('Build Docker') {
            steps {
                sh 'docker build -t master-keycloak .'
            }
        }

        stage('Scan valuable') {
            steps {
                script {
                    def trivyOutput = sh(script:"trivy image master-keycloak", returnStdout: true).trim()
                    println trivyOutput

                    if (trivyOutput.contains("Total:0")) {
                        echo "No Vulnerabilities found in the Docker image."
                    } else {
                        echo "Vulnerabilities found in the Docker image"
                    }
                }
            }
        }
    }
}