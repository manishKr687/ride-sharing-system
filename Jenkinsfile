pipeline {
    agent any

    tools {
        maven 'Maven 3.9.4'  // or whatever version you installed in Jenkins
        jdk 'JDK 17'          // match your project's Java version
    }

    environment {
        PROJECT_DIR = "${WORKSPACE}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/manishKr687/ride-sharing-system.git'
            }
        }

        stage('Build All Microservices') {
            steps {
                echo 'Building all services...'
                sh '''
                cd common-model && mvn clean install
                cd ../ride-service && mvn clean install
                cd ../payment-service && mvn clean install
                cd ../billing-service && mvn clean install
                cd ../notification-service && mvn clean install
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Running unit tests for all services...'
                sh '''
                cd common-model && mvn test
                cd ../ride-service && mvn test
                cd ../payment-service && mvn test
                cd ../billing-service && mvn test
                cd ../notification-service && mvn test
                '''
            }
        }

        stage('Package Microservices') {
            steps {
                echo 'Packaging all services...'
                sh '''
                cd ride-service && mvn package
                cd ../payment-service && mvn package
                cd ../billing-service && mvn package
                cd ../notification-service && mvn package
                '''
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Build and package successful!'
        }
        failure {
            echo 'Build failed. Please check logs.'
        }
    }
}
