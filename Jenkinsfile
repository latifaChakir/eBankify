pipeline {
    agent any

    environment {
        // SonarQube variables
        SONARQUBE_URL = 'http://localhost:9000'
        SONAR_SCANNER = 'SonarScanner' // Nom configuré dans Jenkins
        SONAR_TOKEN = credentials('sonar-token') // Identifiant sécurisé Jenkins

        // Docker variables
        DOCKER_IMAGE = 'ebankify:latest'
    }

    stages {
        // Étape 1 : Cloner le code depuis Git
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'git-credentials', url: 'https://github.com/user/eBankify.git'
            }
        }

        // Étape 2 : Compiler le projet
        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }

        // Étape 3 : Lancer les tests unitaires avec Jacoco
        stage('Test') {
            steps {
                sh './mvnw test'
            }
            post {
                success {
                    // Publier les rapports de couverture
                    jacoco execPattern: '**/target/jacoco.exec'
                }
            }
        }

        // Étape 4 : Analyse de la qualité avec SonarQube
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "./mvnw sonar:sonar -Dsonar.projectKey=eBankify -Dsonar.login=$SONAR_TOKEN"
                }
            }
        }

        // Étape 5 : Conteneuriser avec Docker
        stage('Docker Build') {
            steps {
                sh '''
                docker build -t $DOCKER_IMAGE .
                '''
            }
        }

        // Étape 6 : Validation Manuelle
        stage('Manual Validation') {
            steps {
                input message: "Validez-vous le déploiement de l'application ?", ok: "Déployer"
            }
        }

        // Étape 7 : Déploiement de l'image Docker
        stage('Deploy') {
            steps {
                sh '''
                docker run -d -p 8080:8080 $DOCKER_IMAGE
                '''
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
