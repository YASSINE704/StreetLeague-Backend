pipeline {
    agent any

    environment {
        DOCKER_HUB_ORG = 'streetleague'
        IMAGE_TAG = "${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
    }

    stages {

        stage('Backend') {
            steps {
                dir('backend') {
                    sh './mvnw clean test'
                    sh './mvnw package -DskipTests'
                }
            }
            post { success { archiveArtifacts 'backend/target/*.jar' } }
        }

        stage('Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm ci'
                    sh 'npm run build -- --configuration=production'
                }
            }
            post { success { archiveArtifacts 'frontend/dist/**/*' } }
        }

        stage('AI Service') {
            steps {
                dir('ai-service') {
                    sh 'pip install -r requirements.txt -q'
                    sh 'python -c "from app import app; print(\'AI Service OK\')"'
                }
            }
        }

        stage('Forecast Service') {
            steps {
                dir('forecast-service') {
                    sh 'pip install -r requirements.txt -q'
                    sh 'python -c "from app import app; print(\'Forecast Service OK\')"'
                }
            }
        }

        stage('Docker Build & Push') {
            when { branch 'main' }
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub',
                                                  usernameVariable: 'DOCKER_USER',
                                                  passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker build -t ${DOCKER_HUB_ORG}/backend:${IMAGE_TAG} ./backend
                        docker build -t ${DOCKER_HUB_ORG}/frontend:${IMAGE_TAG} ./frontend
                        docker build -t ${DOCKER_HUB_ORG}/ai-service:${IMAGE_TAG} ./ai-service
                        docker build -t ${DOCKER_HUB_ORG}/forecast-service:${IMAGE_TAG} ./forecast-service

                        docker tag ${DOCKER_HUB_ORG}/backend:${IMAGE_TAG} ${DOCKER_HUB_ORG}/backend:latest
                        docker tag ${DOCKER_HUB_ORG}/frontend:${IMAGE_TAG} ${DOCKER_HUB_ORG}/frontend:latest
                        docker tag ${DOCKER_HUB_ORG}/ai-service:${IMAGE_TAG} ${DOCKER_HUB_ORG}/ai-service:latest
                        docker tag ${DOCKER_HUB_ORG}/forecast-service:${IMAGE_TAG} ${DOCKER_HUB_ORG}/forecast-service:latest

                        echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                        docker push --all-tags ${DOCKER_HUB_ORG}/backend
                        docker push --all-tags ${DOCKER_HUB_ORG}/frontend
                        docker push --all-tags ${DOCKER_HUB_ORG}/ai-service
                        docker push --all-tags ${DOCKER_HUB_ORG}/forecast-service
                    """
                }
            }
        }

        stage('Deploy to K8s') {
            when { branch 'main' }
            steps {
                sh """
                    kubectl set image -n pi-streetleague deployment/backend \
                        backend=${DOCKER_HUB_ORG}/backend:${IMAGE_TAG}
                    kubectl set image -n pi-streetleague deployment/frontend \
                        frontend=${DOCKER_HUB_ORG}/frontend:${IMAGE_TAG}
                    kubectl set image -n pi-streetleague deployment/ai-service \
                        ai-service=${DOCKER_HUB_ORG}/ai-service:${IMAGE_TAG}
                    kubectl set image -n pi-streetleague deployment/forecast-service \
                        forecast-service=${DOCKER_HUB_ORG}/forecast-service:${IMAGE_TAG}

                    kubectl rollout status deployment/backend -n pi-streetleague
                    kubectl rollout status deployment/frontend -n pi-streetleague
                    kubectl rollout status deployment/ai-service -n pi-streetleague
                    kubectl rollout status deployment/forecast-service -n pi-streetleague
                """
            }
        }
    }

    post {
        failure {
            emailext(
                subject: "[CI FAILED] PI_StreetLeague - ${env.BRANCH_NAME}",
                body: "Pipeline failed at stage ${env.STAGE_NAME}. Check ${env.BUILD_URL}",
                to: 'azizeifa74@gmail.com Nafissa.BRIDAH@esprit.tn'
            )
        }
        success {
            echo "Pipeline completed successfully"
        }
    }
}
