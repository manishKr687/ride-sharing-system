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

        stage('Build All Services') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Run Microservices') {
            steps {
                parallel (
					"Common Library": {
                        dir('common-library') {
                            bat 'start cmd /c "java -jar target/common-library-0.0.1-SNAPSHOT.jar"'
                        }
                    },
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
		
		stage('Build Docker Images') {
            steps {
                script {
                    def services = [
						'common-library',
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
