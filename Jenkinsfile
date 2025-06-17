pipeline {
  agent any

  environment {
    BACKEND_IMAGE = "yourname/backend:latest"
  }

  stages {
    stage('Clone Repository') {
      steps {
        git branch: 'main', url: 'https://github.com/ram-nimase/payment-gateway.git'
      }
    }

    stage('Build Spring Boot App') {
      steps {
        dir('backend') {
          sh 'mvn clean package -DskipTests'
        }
      }
    }

    stage('Build Docker Image') {
      steps {
        sh 'docker-compose down'
        sh 'docker image prune -af || true'

        dir('backend') {
          sh "docker build -t $BACKEND_IMAGE ."
        }
      }
    }
     stage('Deploy App') {
          steps {
            sh 'docker-compose up -d --build'
          }
        }
  }

  post {
    always {
      echo 'Cleaning up unused containers...'
      sh 'docker container prune -f || true'
    }
  }
}
