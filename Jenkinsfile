pipeline {
  agent any

  environment {
    IMAGE_NAME = 'foodweek-app'
    CONTAINER_NAME = 'foodweek_container'
    DOCKER_COMPOSE_FILE = 'docker-compose.yml'
  }

  tools {
    maven 'Maven 3.9.4' // Assure-toi que ce nom correspond à l'installation Maven de Jenkins
  }

  stages {

    stage('Clean workspace') {
      steps {
        deleteDir()
        checkout scm
      }
    }

    stage('Build with Maven') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }

    stage('Start MySQL via Docker Compose') {
      steps {
        sh "docker compose up -d mysql"
        echo "Waiting for MySQL to be ready..."
        sh "sleep 20" // Tu peux améliorer ça avec un wait-for-it
      }
    }

    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }

    stage('Build Docker Image') {
      steps {
        sh "docker build -t $IMAGE_NAME ."
      }
    }

    stage('Run Docker Container') {
      steps {
        sh """
          docker rm -f $CONTAINER_NAME || true
          docker run -d --name $CONTAINER_NAME --network=foodweek-network -p 8080:8080 $IMAGE_NAME
        """
      }
    }

  }

  post {
    always {
      echo 'Nettoyage...'
      sh "docker rm -f $CONTAINER_NAME || true"
      sh "docker compose down"
    }
  }
}
