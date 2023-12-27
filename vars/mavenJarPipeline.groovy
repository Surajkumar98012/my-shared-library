def call(Map pipelineParams) {
    pipeline {
        agent any
        stages {
            stage('Build') {
                steps {
                    sh 'mvn clean package'
                }
            }
            stage('Test') {
                steps {
                    sh 'mvn test'
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
