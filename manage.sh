#!/bin/bash
#############################################
#
#	Manage script for the ApPoC application
#
#############################################

# Checking if Docker is installed
if ! which docker; then
       echo "Docker is not installed !"
       exit 1
fi
docker --version

# Checking if Java is present
if ! which java; then
        echo "Java is not present !"
        exit 1
fi
java -version

# Checking if Javac is present
if ! which javac; then
        echo "Javac is not present !"
        exit 1
fi
javac -version

# Checking if Maven is present
if ! ./mvnw --version; then
        echo "Maven is not present !"
        exit 1
fi
export MAVEN_USER_HOME=$HOME/.m2/repository/

#Create a function for docker-compose path of the application
dc() {
    docker-compose -f src/main/docker/app.yml ${@}
}

#Create a function for docker-compose path of monitoring
dc-console() {
    docker-compose -f console/docker-compose.yml ${@}
}

#Create a function for docker-compose path of monitoring
dc-monitoring() {
    docker-compose -f src/main/docker/monitoring.yml ${@}
}

#Log function
log() {
    MESSAGE=$1

    echo "[$0] [$(date +%Y-%m-%dT%H:%M:%S)] ${MESSAGE}"
}


case $1 in

    build-dev)
    log 'Building default profile step ...'
    ./mvnw package -DskipTests jib:dockerBuild;;

    build-prod)
    log 'Building production profile step ...'
    ./mvnw package -Pprod -DskipTests jib:dockerBuild;;

    push)
    log 'Creating the image tag then pushing it to docker ...'
    docker login
    docker image tag appoc monogramm/poc-kafka-docker
    docker push monogramm/poc-kafka-docker
    docker logout;;

    test)
    log 'Executing back-end tests ...'
    ./mvnw -ntp clean verify jacoco:report
    log 'Executing front-end tests ...'
    ./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm -Dfrontend.npm.arguments='run test' -Dmaven.repo.local=$MAVEN_USER_HOME;;

# Application part
    start)
    log 'Starting containers ...'
    dc up -d ${@:2};;

    stop)
    log 'Stoping containers ...'
    dc stop ${@:2};;

    restart)
    log 'Restarting containers ...'
    dc restart ${@:2};;

    remove)
    log 'Stoping and removing containers ...'
    dc down ${@:2};;

    ps)
    log 'Listing all containers ...'
    dc ps ${@:2};;

    logs)
    log 'Displaying the logs ...'
    dc logs -f ${@:2};;

# ELK stack part
    console-start)
    log 'Starting containers ...'
    log 'Warning : As Jhipster switched from Dropwizard Metrics to Micrometer recently, the metrics dashboards are currently broken for applications generated with v5.8.0 and newer. Which is the case here.'
    dc-console up -d ${@:2};;

    console-stop)
    log 'Stoping containers ...'
    dc-console stop ${@:2};;

    console-restart)
    log 'Restarting containers ...'
    dc-console restart ${@:2};;

    console-remove)
    log 'Stoping and removing containers ...'
    dc-console down ${@:2};;

    console-ps)
    log 'Listing all containers ...'
    dc-console ps ${@:2};;

    console-logs)
    log 'Displaying the logs ...'
    dc-console logs -f ${@:2};;

# Prometheus/Grafana part
    monitoring-start)
    log 'Starting containers ...'
    dc-monitoring up -d ${@:2};;

    monitoring-stop)
    log 'Stoping containers ...'
    dc-monitoring stop ${@:2};;

    monitoring-restart)
    log 'Restarting containers ...'
    dc-monitoring restart ${@:2};;

    monitoring-remove)
    log 'Stoping and removing containers ...'
    dc-monitoring down ${@:2};;

    monitoring-ps)
    log 'Listing all containers ...'
    dc-monitoring ps ${@:2};;

    monitoring-logs)
    log 'Displaying the logs ...'
    dc-monitoring logs -f ${@:2};;

# Common part
    start-all)
    log 'Starting containers ...'
    dc up -d ${@:2}
    dc-console up -d ${@:2}
    dc-monitoring up -d ${@:2};;

    stop-all)
    log 'Stoping containers ...'
    dc stop -d ${@:2}
    dc-console stop -d ${@:2}
    dc-monitoring stop ${@:2};;

    restart-all)
    log 'Restarting containers ...'
    dc restart -d ${@:2}
    dc-console restart -d ${@:2}
    dc-monitoring restart ${@:2};;

    remove-all)
    log 'Stoping and removing containers ...'
    dc down -d ${@:2}
    dc-console down -d ${@:2}
    dc-monitoring down ${@:2};;

    ps-all)
    log 'Listing all containers ...'
    dc ps -d ${@:2}
    dc-console ps -d ${@:2}
    dc-monitoring ps ${@:2};;

#Help center
    help|-h|--help|*)
    echo "ApPoC help center"
    echo ""
    echo "manage.sh help                    Display this help"
    echo "manage.sh build-prod              Build the docker package using production profile"
    echo "manage.sh build-dev               Build the docker package using default profile"
    echo "manage.sh push                    Create the image then push it to docker"
    echo "manage.sh test                    Executing the back-end and front-end tests"
    echo ""
    echo "manage.sh start                   Start the application ApPoC (requires docker)"
    echo "manage.sh stop                    Stop the application ApPoC (requires docker)"
    echo "manage.sh restart                 Restart the application ApPoC (requires docker)"
    echo "manage.sh remove                  Stopping and removing containers from tha application (requires docker)"
    echo "manage.sh ps                      List all containers and their healthstate (requires docker)"
    echo "manage.sh logs                    Follow logs of managed application (requires docker)"
    echo ""
    echo "manage.sh console-start           Start the containers for the jhipster-console (requires docker)"
    echo "manage.sh console-stop            Stop the containers for the jhipster-console (requires docker)"
    echo "manage.sh console-restart         Restart the containers for the jhipster-console (requires docker)"
    echo "manage.sh console-remove          Stopping and removing containers for the jhipster-console (requires docker)"
    echo "manage.sh console-ps              List all jhipster-console containers and their healthstate (requires docker)"
    echo "manage.sh console-logs            Follow logs of jhipster-console containers (requires docker)"
    echo ""
    echo "manage.sh monitoring-start        Start the containers for monitoring the application ApPoC (requires docker)"
    echo "manage.sh monitoring-stop         Stop the containers for monitoring the application ApPoC (requires docker)"
    echo "manage.sh monitoring-restart      Restart the containers for monitoring the application ApPoC (requires docker)"
    echo "manage.sh monitoring-remove       Stopping and removing containers from monitoring application (requires docker)"
    echo "manage.sh monitoring-ps           List all monitoring containers and their healthstate (requires docker)"
    echo "manage.sh monitoring-logs         Follow logs of monitoring containers (requires docker)"
    echo ""
    echo "manage.sh start-all               Start all the containers (requires docker)"
    echo "manage.sh stop-all                Stop all the containers (requires docker)"
    echo "manage.sh restart-all             Restart all the containers (requires docker)"
    echo "manage.sh remove-all              Stopping and removing all the containers (requires docker)"
    echo "manage.sh ps-all                  List all containers and their healthstate (requires docker)";;
    
esac


exit 0