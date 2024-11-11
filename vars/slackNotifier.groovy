// vars/slackNotifier.groovy
def call(String message, String color = '#36a64f') {
    slackSend (
        channel: '#jenkins-slack-notifications',
        color: color,
        message: "Success"
    )
}

