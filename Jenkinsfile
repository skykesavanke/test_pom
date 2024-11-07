pipeline {
    agent any

    parameters {
        string(name: 'BUILD_VERSION', defaultValue: '0.0.1-SNAPSHOT', description: 'Build Version')
        string(name: 'BUILD_PROFILE', defaultValue: 'new', description: 'Maven Build Profile')
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
                    // Show Maven version, current directory, and file listing for debugging
                    bat "mvn --version"
                    
                    echo "Build version: ${params.BUILD_VERSION}, Build number: ${params.BUILD_NUMBER}"
                    echo "Versioning POM"
                    
                    // Handling versioning based on BUILD_VERSION or fallback to BUILD_NUMBER
                    if ("${params.BUILD_VERSION}" != '') {
                        bat "mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${params.BUILD_VERSION}"
                    } else {
                        bat "mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${params.BUILD_NUMBER}"
                    }
                    
                    // Commit version changes
                    bat "mvn versions:commit"
                    
                    echo "Building Artifact"
                    
                    // Build artifact with skipping tests and other steps
                    bat "mvn clean package -P${params.BUILD_PROFILE} -Dmaven.test.skip=true -Dexec.skip=true -Dcreate-archive.skip=true"
                    echo "Artifact built successfully"
                    
                    echo "Exporting project version to env vars and passing it to deploy job"
                    
                    // Capture the project version
                    bat "mvn help:evaluate -Dexpression=project.version -q -DforceStdout > env.properties"
                    
                    // Read the project version and clean up any extra whitespace or newlines
                    def PROJECT_VERSION = bat(script: 'type env.properties', returnStdout: true).trim()
                    
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
