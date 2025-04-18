pipeline {
    agent any

    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/manishKr687/ride-sharing-system.git', branch: 'main'
            }
        }

        stage('Build All Microservices') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}
