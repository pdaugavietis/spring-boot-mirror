podTemplate(containers: [
  containerTemplate(name: 'maven', image: 'maven:3.6.0-jdk-8-alpine', ttyEnabled: true, command: 'cat')
  ], volumes: [
  persistentVolumeClaim(mountPath: '/root/.m2/repository', claimName: 'maven-repo', readOnly: false)
  ]) {

  node(POD_LABEL) {
    checkout scm
    stage('Compile and Analysis') {
      parallel 'Compilation': {
        if (isUnix()) {
            sh "./mvnw clean compile -DskipTests"
        } else {
            bat "./mvnw.cmd clean compile -DskipTests"
        }
      } 'Static Analysis': {
        stage("Checkstyle") {
            sh "./mvnw checkstyle:checkstyle"
            
            step([$class: 'CheckStylePublisher',
              canRunOnFailed: true,
              defaultEncoding: '',
              healthy: '100',
              pattern: '**/target/checkstyle-result.xml',
              unHealthy: '90',
              useStableBuildAsReference: true
            ])
        }
        }, 'SonarQube Analysis': {
          stage("Sonarqube") {
            if (isUnix()) {
                sh "SONAR_TOKEN=d1f1cf8d1499413fce7ce3596910c966de2fed3e ./mvnw verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar"
            } else {
                bat "SONAR_TOKEN=d1f1cf8d1499413fce7ce3596910c966de2fed3e ./mvnw verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar"
            }
          }
        }
    }

    stage('Tests and Deployment') {
      parallel 'Unit Tests': {
        stage("Running Unit Tests") {
          try {
              if (isUnix()) {
                  sh "./mvnw test -Punit"
              } else {
                  bat "./mvnw.cmd test -Punit"
              }
          } catch(err) {
              step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*UnitTest.xml'])
              throw err
          }
          step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*UnitTest.xml'])
        }
      }, 'Integration Tests': {
        stage("Running integration tests") {
            try {
                if (isUnix()) {
                    sh "./mvnw test -Pintegration"
                } else {
                    bat "./mvnw.cmd test -Pintegration"
                }
            } catch(err) {
                step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*IntegrationTest.xml'])
                throw err
            }
            step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*IntegrationTest.xml'])
        }
      }
    }

    stage('Generate Artifact Project') {
      container('maven') {
        sh 'mvn -B package'
      }
    }
  }
}