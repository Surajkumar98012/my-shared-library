def call(Map pipelineParams) {
    pipeline {
        agent any
        stages {
            stage('Check Docker Group') {
                steps {
                    script {
                        if (sh(script: 'groups | grep docker', returnStatus: true) != 0) {
                            sh "sudo usermod -aG docker ${pipelineParams.dockerHubUsername}"
                            sh 'newgrp docker'
                        }
                    }
                }
            }
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
                    script {
                        docker.withRegistry('', "${pipelineParams.dockerHubCredentials}") {
                            sh "docker build -t ${pipelineParams.dockerHubUsername}/${pipelineParams.dockerImageName}:${pipelineParams.tag} ."
                            sh "docker push ${pipelineParams.dockerHubUsername}/${pipelineParams.dockerImageName}:${pipelineParams.tag}"
                        }
                    }
                }
            }
        }
    }
}
