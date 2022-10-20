# jenkins-docker-agent

[Build a Java app with Maven](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/)
tutorial in the [Jenkins User Documentation](https://jenkins.io/doc/).

## Key points
- Java, Maven, JUnit simple project to demonstrate Jenkins with Docker
- 3-stages Pipeline (Build, Test, Deliver)
- Docker in Docker to run docker commands into Jenkins
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

## Jenkins with Docker
