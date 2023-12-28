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
                    withCredentials([usernamePassword(credentialsId: pipelineParams.credentialsId, usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        sh "sudo docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
                        sh "sudo docker build -t ${pipelineParams.dockerHubUsername}/${pipelineParams.dockerImageName}:${pipelineParams.tag} ."
                        sh "sudo docker push ${pipelineParams.dockerHubUsername}/${pipelineParams.dockerImageName}:${pipelineParams.tag}"
                    }
                }
            }
        }
    }
}
