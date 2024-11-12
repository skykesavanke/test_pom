pipeline {
    agent any

    parameters {
        string(name: 'BUILD_VERSION', defaultValue: '3', description: 'Build Version')
        string(name: 'BUILD_PROFILE', defaultValue: 'cloud", description: 'Maven Build Profile')
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
                       bat "mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${params.BUILD_VERSION}"
                    
                    } else {
                         bat "mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${params.BUILD_NUMBER}"
                    }
                    
                   
                    bat "mvn versions:commit"
                    
                    echo "Building Artifact"
                    
                    // Build artifact with skipping tests and other steps
                    bat "mvn clean package -P${params.BUILD_PROFILE} -Dmaven.test.skip=true -Dexec.skip=true -Dcreate-archive.skip=true"
                    echo "Artifact built successfully"
                    
                    echo "Exporting project version to env vars and passing it to deploy job"
                    
                    // Capture the project version
                    bat "mvn help:evaluate -Dexpression=project.version -q -DforceStdout > env.properties"
                    
                    // Read the project version and clean up any extra whitespace or newlines
                    def PROJECT_VERSION = bat(script: 'cat env.properties', returnStdout: true)
                    
                    echo "Artifact version is ${PROJECT_VERSION}"

                    // Optionally, store version as an environment variable or pass it to subsequent jobs
                    env.PROJECT_VERSION = PROJECT_VERSION
                    
                }
            }
        }
    }

    post {
        success {
            echo "Build and versioning successful"
        }
        failure {
            echo "Build failed"
        }
    }
}
