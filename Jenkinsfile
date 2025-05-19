pipeline {
    agent any

    environment {
        // define environment variable
        // Jenkins credentials configuration
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub_credentials') // Docker Hub credentials ID store in Jenkins
        // Docker Hub Repository's name
        DOCKER_IMAGE = 'linzhengyu36/teedy' // your Docker Hub user name and Repository's name
        DOCKER_TAG = "${env.BUILD_NUMBER}" // use build number as tag
    }

    stages {
        stage('Build') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/master']],
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/lzy2005/Teedy.git']] // your github Repository
                )
                sh 'mvn -B -DskipTests clean package'
            }
        }

        // Building Docker images
        stage('Building image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub_credentials') {
                        docker.build("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                    }
                }
            }
        }

        // Uploading Docker images into Docker Hub
        stage('Upload image') {
            steps {
                script {
                    // sign in Docker Hub
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub_credentials') {
                        // push image
                        docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").push()

                        // ：optional: label latest
                        // docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").push('latest')
                    }
                }
            }
        }

        // Running Docker containers
        stage('Run containers') {
            steps {
                script {
                    // Define the ports and container names
                    def ports = [8082, 8083, 8084]
                    def containerNames = ports.collect { port -> "teedy-container-${port}" }

                    // Stop and remove existing containers
                    containerNames.each { containerName ->
                        sh "docker stop ${containerName} || true"
                        sh "docker rm ${containerName} || true"
                    }

                    // Run containers
                    ports.each { port ->
                        docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                            .run("--name teedy-container-${port} -d -p ${port}:8080")
                    }

                    // Optional: list all teedy-containers
                    sh 'docker ps --filter "name=teedy-container"'
                }
            }
        }
    }
}