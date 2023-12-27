def call(Map pipelineParams) {
    pipeline {
        agent any
        stages {
            stage('Build') {
                steps {
                    sh 'javac HelloWorld.java'
                }
            }
            stage('Test') {
                steps {
                    sh 'java HelloWorld'
                }
            }
            stage('Push') {
                steps {
                    withCredentials([usernamePassword(credentialsId: '7422db48-acba-4dea-9b0d-0a17990192f9', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "sudo docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                        sh "sudo docker build -t ${pipelineParams.dockerHubUsername}/${pipelineParams.dockerImageName}:${pipelineParams.tag} ."
                        sh "sudo docker push ${pipelineParams.dockerHubUsername}/${pipelineParams.dockerImageName}:${pipelineParams.tag}"
                    }
                }
        }
    }
}
}
