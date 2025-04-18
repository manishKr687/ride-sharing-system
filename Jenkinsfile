pipeline {
    agent any

    tools { 
		git 'GIT_HOME'
        maven 'Apache Maven'
        jdk 'JAVA_HOME'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
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
