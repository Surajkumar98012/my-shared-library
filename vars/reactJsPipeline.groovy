// reactJsPipeline.groovy
def call(Map config) {
    // Define the pipeline parameters
    def dockerImage = config.dockerImage ?: 'node:14.17.0'
    def dockerHubUser = config.dockerHubUser ?: 'username'
    def dockerHubRepo = config.dockerHubRepo ?: 'my-react-app'
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
            stage('Install') {
                steps {
                    // Run the npm install command
                    sh 'npm install'
                }
            }
            stage('Build') {
                steps {
                    // Run the npm build command
                    sh 'npm run build'
                }
            }
            stage('Test') {
                steps {
                    // Run the npm test command
                    sh 'npm test'
                }
            }
            stage('Push') {
                steps {
                    // Access the docker hub credentials
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        // Login to docker hub
                        sh "docker login -u $DOCKER_USER -p $DOCKER_PASS"
                        // Build the docker image with the build folder
                        sh "docker build -t $dockerHubUser/$dockerHubRepo:$dockerHubTag ."
                        // Push the docker image to docker hub
                        sh "docker push $dockerHubUser/$dockerHubRepo:$dockerHubTag"
                    }
                }
            }
        }
    }
}
