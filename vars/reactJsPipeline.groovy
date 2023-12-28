pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Dockerize') {
            steps {
                script {
                    dockerImage = docker.build("${pipelineParams.dockerHubUsername}/${pipelineParams.dockerImageName}:${pipelineParams.tag}")
                }
            }
        }

        stage('Push image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        dockerImage.push("latest")
                    }
                }
            }
        }
    }
}
