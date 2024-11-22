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

        stage('Scan Image Valuable') {
            script {
            steps {
            def trivyOutput = sh(script: "trivy image master-keycloak", returnStdout: true).trim()
            println trivyOutput

            if (trivyOutput.contains("Total:0")) {
                echo "No vulnerabilities found in the Docker image."
            } else {
            echo "Vulnerabilities found in the Docker image."
            }
            }
            }
        }
//             stage('Login to Docker Hub'){
//             steps {
//                 script {
//                     echo 'Login to Docker Hub'
//                     docker.withRegistry('https://docker.io', 'dockerhub-credential') {
//                     def customImage = docker.build("identity_service:${IMAGE_TAG}")
//
//                             /* Push the container to the custom Registry */
//                             customImage.push()
//                     }
//                 }
//             }
//         }
//         stage('Push to Registry') {
//             steps {
//                 script {
//                 echo 'Tag and push docker image'
//                 sh "docker tag ${DOCKER_IMAGE} phamminhtan/identity_service:${IMAGE_TAG}"
//                 sh "docker push phamminhtan/identity_service:${IMAGE_TAG}"
//                 }
//             }
//         }

    }
}