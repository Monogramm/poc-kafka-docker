#!/usr/bin/env groovy

node {
    parameters {
        string(name: 'DOCKER_USERNAME', defaultValue: 'Kamoulox', description: 'Enter a username')

        password(name: 'DOCKER_PASSWORD', defaultValue: 'SECRET', description: 'Enter a password')
    }
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "chmod +x mvnw"
        sh "./mvnw -ntp clean"
    }
    stage('nohttp') {
        sh "./mvnw -ntp checkstyle:check"
    }

    stage('install tools') {
        sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:install-node-and-npm -DnodeVersion=v12.14.0 -DnpmVersion=6.13.7"
    }

    stage('npm install') {
        sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm"
    }

    stage('backend tests') {
        try {
            sh "./mvnw -ntp verify"
        } catch(err) {
            throw err
        } finally {
            junit '**/target/test-results/**/TEST-*.xml'
        }
    }

    stage('frontend tests') {
        sh "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm -Dfrontend.npm.arguments='run test'"
    }

    stage('packaging') {
        sh "./mvnw -ntp verify -Pprod -DskipTests"
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
    }

    stage('Build the docker image') {
        sh "./mvnw package -Pprod -DskipTests jib:dockerBuild"
        sh "docker login --username $DOCKER_USERNAME --password $DOCKER_PASSWORD"
        sh "docker image tag appoc monogramm/poc-kafka-docker"
    }

    stage ('Push the image to Docker Hub') {
        sh "docker push monogramm/poc-kafka-docker"
        sh "docker logout"
    }
}
