podTemplate(
	yaml:'''
spec:
  containers:
  - name: jnlp
    image: jenkins/jnlp-slave:4.0.1-1
    volumeMounts:
    - name: home-volume
      mountPath: /home/jenkins
    env:
    - name: HOME
      value: /home/jenkins
  - name: maven
    image: maven:3.6.3-jdk-8
    command: ['cat']
    tty: true
    volumeMounts:
    - name: home-volume
      mountPath: /home/jenkins
    env:
    - name: HOME
      value: /home/jenkins
    - name: MAVEN_OPTS
      value: -Duser.home=/home/jenkins
  volumes:
  - name: home-volume
    emptyDir: {}
''') {

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
