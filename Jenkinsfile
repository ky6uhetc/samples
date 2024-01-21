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
                dir('architectures_and_design_patterns') {
                    withGradle {
                        sh './gradlew clean build test'
                    }
                }
            }
        }
    }
    post {
        always {
            junit 'build/test-results/**/*.xml'
        }
    }
}
