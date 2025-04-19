pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-real-username/ride-sharing-system.git'
            }
        }

        stage('Build Billing Service') {
            steps {
                dir('billing-service') {
                    bat 'mvn clean install'
                }
            }
        }

        stage('Build Notification Service') {
            steps {
                dir('notification-service') {
                    bat 'mvn clean install'
                }
            }
        }

        stage('Build Payment Service') {
            steps {
                dir('payment-service') {
                    bat 'mvn clean install'
                }
            }
        }

        stage('Build Ride Service') {
            steps {
                dir('ride-service') {
                    bat 'mvn clean install'
                }
            }
        }

        stage('Build User Service') {
            steps {
                dir('user-service') {
                    bat 'mvn clean install'
                }
            }
        }
    }
}
