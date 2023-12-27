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
                    sh "docker build -t ${pipelineParams.dockerHubUsername}/${pipelineParams.dockerImageName}:${pipelineParams.tag} ."
                    sh "docker push ${pipelineParams.dockerHubUsername}/${pipelineParams.dockerImageName}:${pipelineParams.tag}"
                }
            }
        }
    }
}
