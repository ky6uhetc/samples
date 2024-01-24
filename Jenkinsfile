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
    }
}
