pipeline {
  agent any

  environment {
    IMAGE_NAME = 'foodweek-app'
    CONTAINER_NAME = 'foodweek_container'
    VPS_HOST = 'root@<IP_DU_VPS>' // à adapter
  }

  tools {
    jdk 'jdk21'
    maven 'Maven 3.9.4'
  }

  stages {

    stage('🧹 Clean + Checkout') {
      steps {
        deleteDir()
        checkout scm
      }
    }

    stage('🔨 Build') {
      steps {
        sh 'mvn clean compile'
      }
    }

    stage('✅ Test unitaire') {
      steps {
        sh 'mvn test'
      }
    }

    stage('📦 Package') {
      steps {
        sh 'mvn package -DskipTests'
      }
    }

    stage('🐳 Docker Build') {
      steps {
        sh "docker build -t $IMAGE_NAME ."
      }
    }

    stage('🚀 Deploy to VPS') {
      steps {
        sshagent(credentials: ['vps-deploy-key']) {
          sh """
            ssh -o StrictHostKeyChecking=no $VPS_HOST '
              docker stop $CONTAINER_NAME || true &&
              docker rm -f $CONTAINER_NAME || true &&
              docker rmi $IMAGE_NAME || true &&
              mkdir -p /app/foodweek
            '

            scp -r . $VPS_HOST:/app/foodweek/

            ssh $VPS_HOST '
              cd /app/foodweek &&
              docker build -t $IMAGE_NAME . &&
              docker run -d --name $CONTAINER_NAME -p 80:8080 $IMAGE_NAME
            '
          """
        }
      }
    }
  }

  post {
    failure {
      echo "❌ Le pipeline a échoué. Vérifie les étapes précédentes."
    }
    success {
      echo "✅ Déploiement réussi sur le VPS 🎉"
    }
  }
}
