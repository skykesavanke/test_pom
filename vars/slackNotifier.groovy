

def slackNotifier(String buildStatus) {
    // Build status logic as per your function
    buildStatus = buildStatus ?: 'STARTED'

    def colorCode = '#FF0000'
    if (buildStatus == 'SUCCESS') {
        colorCode = '#36A64F'
    } else if (buildStatus == 'UNSTABLE') {
        colorCode = '#FFFF00'
    }

    def message = "*${buildStatus}:* Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
    slackSend(color: colorCode, message: message, channel: "#${env.SLACK_CHANNEL}")
}
