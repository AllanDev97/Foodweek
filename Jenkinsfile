pipeline {
  agent any

  environment {
    IMAGE_NAME = 'foodweek-app'
    CONTAINER_NAME = 'foodweek_container'
  }

  stages {
    stage('Build') {
      steps {
        sh 'java -version'
        sh 'mvn -v'
        sh 'mvn clean package -DskipTests'
      }
    }

    stage('Docker Build') {
      steps {
        sh "docker build -t $IMAGE_NAME ."
      }
    }

    stage('Run App') {
      steps {
        sh "docker run -d --rm --name $CONTAINER_NAME -p 8080:8080 $IMAGE_NAME"
      }
    }
  }

  post {
    always {
      sh "docker rm -f $CONTAINER_NAME || true"
    }
  }
}
