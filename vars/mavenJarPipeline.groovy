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
                    sh 'docker build -t <docker-hub-username>/<docker-image-name>:<tag> .'
                    sh 'docker push <docker-hub-username>/<docker-image-name>:<tag>'
                }
            }
        }
    }
}
