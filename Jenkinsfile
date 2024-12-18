pipeline {
    agent any

    tools {
        maven 'Maven' // Assurez-vous que Maven est configuré dans Jenkins
    }

    environment {
        DOCKER_IMAGE = 'banking-system'
        DOCKER_TAG = "${BUILD_NUMBER}"
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    deleteDir() // Nettoyer le workspace
                    echo "Clonage du dépôt Git..."
                    sh '''
                        git clone -b devops https://github.com/latifaChakir/eBankify .
                        echo "Dépôt cloné avec succès."
                    '''
                }
            }
        }

        stage('Environment Check') {
            steps {
                sh '''
                    echo "Version de Git :"
                    git --version
                    echo "Branche Git actuelle :"
                    git branch --show-current
                    echo "Statut de Git :"
                    git status
                    echo "Version de Java :"
                    java -version
                    echo "Version de Javac :"
                    javac -version
                    echo "Contenu du répertoire de travail :"
                    pwd
                    ls -la
                '''
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Quality Analysis') {
            steps {
                sh '''
                    mvn sonar:sonar \
                        -Dsonar.projectKey=Bank \
                        -Dsonar.projectName=Bank \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=${SONAR_TOKEN}
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                    docker.build("${DOCKER_IMAGE}:latest")
                }
            }
        }

        stage('Manual Approval') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    input message: 'Déployer en production ?', ok: 'Procéder'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").run('-p 8081:8080')
                }
            }
        }
    }

    post {
        success {
            mail to: 'chakirlatifa2001@gmail.com',
                 subject: "Pipeline Success - eBankify",
                 body: "Le pipeline Jenkins s'est terminé avec succès !"
        }
        failure {
            mail to: 'chakirlatifa2001@gmail.com',
                 subject: "Pipeline Failure - eBankify",
                 body: "Le pipeline Jenkins a échoué. Veuillez vérifier les logs."
        }
    }
}
