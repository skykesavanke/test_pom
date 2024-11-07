pipeline{
    stages{
        stage("checkout"){
            gitVars = checkout([$class: 'GitSCM',
            branches: [[name: "refs/heads/main"]],
            extensions: [[$class: 'SubmoduleOption', recursiveSubmodules: true, parentCredentials : true]],
            userRemoteConfigs: [[url: "https://github.com/skykesavanke/test_pom.git"]]
            ])
        }
        stage("build"){

        bat "mvn --version"
            bat "pwd"
            bat 'ls -al'
			echo "Versioning POM"
			if ("${BUILD_VERSION}" != '') {
                bat "mvn build-helper:parse-version versions:set -DnewVersion=\$\\{parsedVersion.majorVersion}.\$\\{parsedVersion.minorVersion}.\\${BUILD_VERSION}"
            } 
			else {
                bat "mvn build-helper:parse-version versions:set -DnewVersion=\$\\{parsedVersion.majorVersion}.\$\\{parsedVersion.minorVersion}.\\${BUILD_NUMBER}"
            }
			bat "mvn versions:commit"
            echo "Building Artifact"
			bat "mvn clean package -P${BUILD_PROFILE} -Dmaven.test.skip=true -Dexec.skip=true -Dcreate-archive.skip=true"
			echo "Artifact built successfully"
			echo("Exporting project version to env vars and pass it to deploy job")
            bat "mvn help:evaluate -Dexpression=project.version -q -DforceStdout > env.properties"
		    PROJECT_VERSION = bat(script: 'cat env.properties', returnStdout: true)
            echo "Artifact version is ${PROJECT_VERSION}"
    }
}}