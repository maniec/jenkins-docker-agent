# jenkins-docker-agent

[Build a Java app with Maven](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/)
tutorial in the [Jenkins User Documentation](https://jenkins.io/doc/).

## Key points
- Java, Maven, JUnit simple project to demonstrate Jenkins with Docker
- 3-stages Pipeline (Build, Test, Deliver)
- Docker in Docker to run docker commands inside Jenkins
- Jenkins Docker agent

## Fork & Clone
[Fork](https://docs.github.com/en/get-started/quickstart/fork-a-repo)
from this [repo](https://github.com/jenkins-docs/simple-java-maven-app)
and Clone into your workspace

## Sync Upstream
    git remote -v 
        origin  https://github.com/maniec/jenkins-docker-agent.git (fetch)
        origin  https://github.com/maniec/jenkins-docker-agent.git (push)
    git remote add upstream https://github.com/jenkins-docs/simple-java-maven-app.git
    git remote -v
        origin  https://github.com/maniec/jenkins-docker-agent.git (fetch)
        origin  https://github.com/maniec/jenkins-docker-agent.git (push)
        upstream        https://github.com/jenkins-docs/simple-java-maven-app.git (fetch)
        upstream        https://github.com/jenkins-docs/simple-java-maven-app.git (push)

## 1 - Docker Network
    docker network create jenkins

## 2 - Docker in Docker
- To execute Docker commands inside Jenkins nodes
- Need **_docker:dind_** Docker image


    docker run \
    --name jenkins-docker \
    --rm \
    --detach \
    --privileged \
    --network jenkins \
    --network-alias docker \
    --env DOCKER_TLS_CERTDIR=/certs \
    --volume jenkins-docker-certs:/certs/client \
    --volume jenkins-data:/var/jenkins_home \
    --publish 3000:3000 \
    --publish 5000:5000 \
    --publish 2376:2376 \
    docker:dind \
    --storage-driver overlay2

## 3 - Jenkins as a Docker container
- Customise official Jenkins Docker image
- Based on the jenkins/jenkins Docker image


    FROM jenkins/jenkins:2.361.2-jdk11
    USER root
    RUN apt-get update && apt-get install -y lsb-release
    RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
    https://download.docker.com/linux/debian/gpg
    RUN echo "deb [arch=$(dpkg --print-architecture) \
    signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
    https://download.docker.com/linux/debian \
    $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
    RUN apt-get update && apt-get install -y docker-ce-cli
    USER jenkins
    RUN jenkins-plugin-cli --plugins "blueocean:1.25.8 docker-workflow:521.v1a_a_dd2073b_2e"

## 4 - Build customized Jenkins image
    docker build -t jenkins-blueocean:2.361.2-1 .

## 5 - Run customized Jenkins image
    docker run \
    --name jenkins-blueocean \
    --detach \
    --network jenkins \
    --env DOCKER_HOST=tcp://docker:2376 \
    --env DOCKER_CERT_PATH=/certs/client \
    --env DOCKER_TLS_VERIFY=1 \
    --publish 8080:8080 \
    --publish 50000:50000 \
    --volume jenkins-data:/var/jenkins_home \
    --volume jenkins-docker-certs:/certs/client:ro \
    --volume "$HOME":/home \
    --restart=on-failure \
    --env JAVA_OPTS="-Dhudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT=true" \
    jenkins-blueocean:2.361.2-1

## Error 1
    WorkflowScript: 3: Invalid agent type "docker" specified. Must be one of [any, label, none] @ line 3, column 9.
    docker { image 'ubuntu:18.04' }
### Solution
You have to install 2 plugins:
- Docker
- Docker Pipeline

## Error 2
    ERROR: Checkout of Git remote '/Users/gschipper/workspace/Docker/jenkins-docker-agent' 
    aborted because it references a local directory, which may be insecure
    You can allow local checkouts anyway by setting the system property 'hudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT' to true
### Solution

    brew info jenkins-lts

    ==> Caveats
    Note: When using launchctl the port will be 8080. 
    To restart jenkins-lts after an upgrade:
    brew services restart jenkins-lts
    Or, if you don't want/need a background service you can just run:
    /opt/homebrew/opt/openjdk@17/bin/java -Dmail.smtp.starttls.enable=true -jar /opt/homebrew/opt/jenkins-lts/libexec/jenkins.war --httpListenAddress=127.0.0.1 --httpPort=8080

    brew services stop jenkins-lts
    /opt/homebrew/opt/openjdk@17/bin/java -Dhudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT=true -Dmail.smtp.starttls.enable=true -jar /opt/homebrew/opt/jenkins-lts/libexec/jenkins.war --httpListenAddress=127.0.0.1 --httpPort=8080 &

### Stop, start
https://damien.co/blog/2012-11-28-how-to-start-stop-restart-or-reload-jenkins-mac-osx/

    ps -e | grep jenkins