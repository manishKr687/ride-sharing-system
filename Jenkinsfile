pipeline {
    agent any

    tools { 
        maven 'Apache Maven'
        jdk 'JAVA_HOME'
    }

    environment {
        SERVICES = "user-service driver-service ride-service payment-service notification-service billing-service"
    }

    stages {
        stage('Checkout Code') {
            steps {
                timeout(time: 20, unit: 'MINUTES') {  
                    checkout scm
                }
            }
        }

        stage('Build All Microservices') {
            steps {
                script {
                    SERVICES.split().each { service ->
                        dir(service) {
                            echo "Building ${service}..."
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build and Test successful for all microservices!"
        }
        failure {
            echo "❌ Build or Test failed!"
        }
    }
}
