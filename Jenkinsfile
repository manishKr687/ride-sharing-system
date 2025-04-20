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
                bat 'mvn clean install -DskipTests -Dmaven.javadoc.skip=true'
            }
        }
    }
}
