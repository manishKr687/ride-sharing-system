pipeline {
    agent any

    environment {
        REGISTRY = "ride-sharing-system"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/manishKr687/ride-sharing-system.git'
            }
        }

        stage('Build Maven Projects') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def services = [
						'common-library'
                        'user-service',
                        'driver-service',
                        'ride-service',
                        'payment-service',
                        'billing-service',
                        'notification-service'
                    ]
                    for (service in services) {
                        dir(service) {
                            sh "docker build -t ${REGISTRY}-${service}:latest ."
                        }
                    }
                }
            }
        }

        stage('Docker Compose Up') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
        }
    }
}
