@Library('slack') _
pipeline {
    agent any
    environment {
        SLACK_CHANNEL = 'keerthusspace' // Slack channel to send notifications
    }

    parameters {
        string(name: 'BUILD_VERSION', defaultValue: '3', description: 'Build Version')
        string(name: 'BUILD_PROFILE', defaultValue: 'cloud', description: 'Maven Build Profile')
        string(name: 'BUILD_NUMBER', defaultValue: '2', description: 'Maven Build Number')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/skykesavanke/test_pom.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    
                    bat "mvn --version"
                    
                    echo "Build version: ${params.BUILD_VERSION}, Build number: ${params.BUILD_NUMBER}"
                    echo "Versioning POM"
                    
                    
                     if ("${params.BUILD_VERSION}" != '') {
                       bat "mvn build-helper:parse-version versions:set -Dmaven.repo.local=${env.WORKSPACE}/.m2/repository -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.${params.BUILD_VERSION}"
                    
                    } else {
                         bat "mvn build-helper:parse-version versions:set -Dmaven.repo.local=${env.WORKSPACE}/.m2/repository -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.${params.BUILD_NUMBER}"
                    }
                    
                   
                    bat "mvn versions:commit -Dmaven.repo.local=${env.WORKSPACE}/.m2/repository"
                    
                    echo "Building Artifact"
                    
                    // Build artifact with skipping tests and other steps
                    bat "mvn clean package -Dmaven.repo.local=${env.WORKSPACE}/.m2/repository -P${params.BUILD_PROFILE} -Dmaven.test.skip=true -Dexec.skip=true -Dcreate-archive.skip=true"
                    echo "Artifact built successfully"
                    
                    echo "Exporting project version to env vars and passing it to deploy job"
                    
                    // Capture the project version
                    bat "mvn help:evaluate -Dmaven.repo.local=${env.WORKSPACE}/.m2/repository -Dexpression=project.version -q -DforceStdout > env.properties"
                    
                    // Read the project version and clean up any extra whitespace or newlines
                    def PROJECT_VERSION = bat(script: 'type env.properties', returnStdout: true)
                    
                    echo "Artifact version is ${PROJECT_VERSION}"

                    // Optionally, store version as an environment variable or pass it to subsequent jobs
                    env.PROJECT_VERSION = PROJECT_VERSION
                    
                }
            }
        }
    }

     post {
        success {
            
                sendNotification('SUCCESS')
            
        }
        failure {
            
                sendNotification('FAILURE')
            
        }
        unstable {
            
                sendNotification('UNSTABLE')
            
        }
     }
}
    def sendNotification(status) {
    // Add your notification logic here, e.g., send a message to Slack
    echo "Notification sent with status: ${status}"
}


