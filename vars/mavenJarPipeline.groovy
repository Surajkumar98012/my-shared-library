// mavenJarPipeline.groovy
def call(Map config) {
    // Define the pipeline parameters
    def dockerImage = config.dockerImage ?: 'maven:3.8.1-jdk-11'
    def dockerHubUser = config.dockerHubUser ?: 'suraj009' // Change the default value to 'suraj009'
    def dockerHubRepo = config.dockerHubRepo ?: 'my-java-app'
    def dockerHubTag = config.dockerHubTag ?: 'latest'

    // Define the pipeline stages and steps
    pipeline {
        agent {
            // Use a docker container to run the commands
            docker {
                image dockerImage
                reuseNode true
            }
        }
        stages {
            stage('Build') {
                steps {
                    // Run the maven build command
                    sh 'mvn clean package'
                }
            }
            stage('Test') {
                steps {
                    // Run the maven test command
                    sh 'mvn test'
                }
            }
            stage('Push') {
                steps {
                    // Access the docker hub credentials
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        // Login to docker hub
                        sh "docker login -u $DOCKER_USER -p $DOCKER_PASS"
                        // Build the docker image with the jar file
                        sh "docker build -t $dockerHubUser/$dockerHubRepo:$dockerHubTag ."
                        // Push the docker image to docker hub
                        sh "docker push $dockerHubUser/$dockerHubRepo:$dockerHubTag"
                    }
                }
            }
        }
    }
}
