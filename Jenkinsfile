podTemplate(containers: [
  containerTemplate(name: 'maven', image: 'maven:3.6.0-jdk-8-alpine', ttyEnabled: true, command: 'cat')
  ], volumes: [
  persistentVolumeClaim(mountPath: '/root/.m2/repository', claimName: 'maven-repo', readOnly: false)
  ]) {

  node(POD_LABEL) {
    checkout scm
    stage('Compile and Analysis') {
      parallel 'Compilation': {
        container("maven") {
          if (isUnix()) {
              sh "mvn compile -DskipTests -Dmaven.test.skip=true"
          } else {
              bat "mvn.cmd compile -DskipTests -Dmaven.test.skip=true"
          }
        }
      }, 'Static Analysis': {
        stage("Checkstyle") {
          echo "CheckStyle here..."
        }
        }, 'SonarQube Analysis': {
          stage("Sonarqube") {
            echo "Scanning here..."
          }
        }
    }

    stage('Tests and Deployment') {
      parallel 'Unit Tests': {
        stage("Running Unit Tests") {
          echo "IntegrationTest here..."
//           container("maven") {
//             try {
//                 if (isUnix()) {
//                     sh "mvn test -Punit"
//                 } else {
//                     bat "mvn.cmd test -Punit"
//                 }
//             } catch(err) {
//                 step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*UnitTest.xml'])
//                 throw err
//             }
//             step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*UnitTest.xml'])
//           }
        }
      }, 'Integration Tests': {
        stage("Running integration tests") {
          echo "IntegrationTest here..."
//           container("maven") {
//               try {
//                 if (isUnix()) {
//                     sh "mvn test -Pintegration"
//                 } else {
//                     bat "mvn.cmd test -Pintegration"
//                 }
//             } catch(err) {
//                 step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*IntegrationTest.xml'])
//                 throw err
//             }
//             step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*IntegrationTest.xml'])
//           }
        }
      }
    }

    stage('Generate Artifact Project') {
      container('maven') {
        container("maven") {
          sh 'mvn -B package'
        }
      }
    }

    stage('Deploy to Nexus') {
      container('maven') {
        container("maven") {
          sh 'mvn -B deploy'
        }
      }
    }

  }
}
