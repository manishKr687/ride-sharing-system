pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/manishKr687/ride-sharing-system.git'
            }
        }

        stage('Build All Services') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Run Microservices') {
            steps {
                parallel (
                    "User Service": {
                        dir('user-service') {
                            bat 'start cmd /c "java -jar target/user-service-0.0.1-SNAPSHOT.jar"'
                        }
                    },
                    "Driver Service": {
                        dir('driver-service') {
                            bat 'start cmd /c "java -jar target/driver-service-0.0.1-SNAPSHOT.jar"'
                        }
                    },
                    "Ride Service": {
                        dir('ride-service') {
                            bat 'start cmd /c "java -jar target/ride-service-0.0.1-SNAPSHOT.jar"'
                        }
                    },
                    "Notification Service": {
                        dir('notification-service') {
                            bat 'start cmd /c "java -jar target/notification-service-0.0.1-SNAPSHOT.jar"'
                        }
                    },
                    "Payment Service": {
                        dir('payment-service') {
                            bat 'start cmd /c "java -jar target/payment-service-0.0.1-SNAPSHOT.jar"'
                        }
                    },
                    "Billing Service": {
                        dir('billing-service') {
                            bat 'start cmd /c "java -jar target/billing-service-0.0.1-SNAPSHOT.jar"'
                        }
                    }
                )
            }
        }
    }
}
