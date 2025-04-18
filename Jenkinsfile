pipeline {
    agent any

    tools { 
        maven 'Apache Maven'
        jdk 'JAVA_HOME'
		git 'GIT_HOME'
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
