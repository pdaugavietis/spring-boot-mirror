podTemplate(containers: [
  containerTemplate(name: 'maven', image: 'maven:3.6.0-jdk-8-alpine', ttyEnabled: true, command: 'cat')
  ], volumes: [
  persistentVolumeClaim(mountPath: '/root/.m2/repository', claimName: 'maven-repo', readOnly: false)
  ]) {

  node(POD_LABEL) {
    checkout scm
    stage('Build Project') {
      container('maven') {
        sh 'mvn -B clean compile'
      }
    }
    stage('Test Project') {
      container('maven') {
        sh 'mvn -B test'
      }
    }
    stage('Generate Artifact Project') {
      container('maven') {
        sh 'mvn -B package'
      }
    }
    logstashSend failBuild: false, maxLines: 1000
  }
}
