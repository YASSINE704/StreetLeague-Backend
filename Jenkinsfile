pipeline {
    agent any

    environment {
        DOCKER_HUB_ORG = 'streetleague'
        IMAGE_TAG = "${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
        KUBECONFIG = '/home/jenkins/.kube/config'
    }

    stages {

        stage('Backend') {
            steps {
                sh '''
                    docker run -d \
                      --name mysql-test \
                      -e MYSQL_ROOT_PASSWORD=root \
                      -e MYSQL_DATABASE=streetleague \
                      mysql:8
                '''
                sh '''
                    echo "Waiting for MySQL to be ready..."
                    for i in $(seq 1 30); do
                        if docker exec mysql-test mysqladmin ping -h 127.0.0.1 -u root -proot --silent 2>/dev/null; then
                            echo "MySQL is up!"
                            break
                        fi
                        echo "Attempt $i/30 - MySQL not ready yet, waiting 3s..."
                        sleep 3
                    done
                '''
                script {
                    env.MYSQL_IP = sh(
                        script: "docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mysql-test",
                        returnStdout: true
                    ).trim()
                    echo "MySQL IP: ${env.MYSQL_IP}"
                }
                dir('backend') {
                    sh 'chmod +x mvnw'
                    sh "./mvnw clean test -Dspring.datasource.url=jdbc:mysql://${env.MYSQL_IP}:3306/streetleague -Dspring.datasource.username=root -Dspring.datasource.password=root"
                    sh "./mvnw package -DskipTests -Dspring.datasource.url=jdbc:mysql://${env.MYSQL_IP}:3306/streetleague -Dspring.datasource.username=root -Dspring.datasource.password=root"
                }
            }
            post {
                always {
                    sh 'docker rm -f mysql-test || true'
                }
                success {
                    archiveArtifacts 'backend/target/*.jar'
                }
            }
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
                    sh 'python3 -m venv venv'
                    sh '. venv/bin/activate && pip install -r requirements.txt -q'
                    sh '. venv/bin/activate && python -c "from app import app; print(\'AI Service OK\')"'
                }
            }
        }

        stage('Forecast Service') {
            steps {
                dir('forecast-service') {
                    sh 'python3 -m venv venv'
                    sh '. venv/bin/activate && pip install -r requirements.txt -q'
                    sh '. venv/bin/activate && python -c "from app import app; print(\'Forecast Service OK\')"'
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
                to: 'azizeifa74@gmail.com, Nafissa.BRIDAH@esprit.tn'
            )
        }
        success {
            echo "Pipeline completed successfully"
        }
    }
}
