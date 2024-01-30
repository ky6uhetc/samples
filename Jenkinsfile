void setBuildStatus(String message, String context, String state) {
    withCredentials([string(credentialsId: 'github-ky6uhetc-oauth', variable: 'TOKEN')]) {
        sh """
            set -x
            curl \"https://api.github.com/repos/ky6uhetc/samples/statuses/$GIT_COMMIT\" \
                -X POST \
                -H \"Accept: application/vnd.github+json\" \
                -H \"Authorization: Bearer $TOKEN\" \
                -H \"Content-Type: application/json\" \
                -X POST \
                -d \"{\\\"description\\\": \\\"$message\\\", \\\"state\\\": \\\"$state\\\", \\\"context\\\": \\\"$context\\\", \\\"target_url\\\": \\\"$BUILD_URL\\\"}\"
        """
    }
}

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '3', artifactNumToKeepStr: '3'))
        disableConcurrentBuilds()
        disableResume()
    }
    stages {
        stage('Build and test architectures_and_design_patterns') {
            steps {
                setBuildStatus("Building and testing", "continuous-integration/jenkins", "pending");
                dir('architectures_and_design_patterns') {
                    withGradle {
                        sh './gradlew clean build test'
                    }
                    recordIssues tool:
                        detekt(pattern: '**/reports/**/detekt.xml', reportEncoding: 'UTF-8'),
                        qualityGates: [[threshold: 10, type: 'TOTAL', unstable: true], [threshold: 100, type: 'TOTAL', unstable: false]]
                    jacoco(
                        classPattern: '**/classes/kotlin/main',
                        sourcePattern: '**/src/main/kotlin',
                        sourceInclusionPattern: '**/*.kt'
                    )
                }
            }
        }
    }
    post {
        always {
            junit '**/build/test-results/**/*.xml'
        }
        success {
            setBuildStatus("Build and test is successfully complete", "continuous-integration/jenkins", "success");
        }
        unstable {
            setBuildStatus("Build and test has completed with some errors", "continuous-integration/jenkins", "error");
        }
        failure {
            setBuildStatus("Build and test has failed", "continuous-integration/jenkins", "failure");
        }
    }
}
