FROM jenkins/jenkins:2.263.4-lts

# Elevate to install packages
USER root

RUN apt-get update && \
    apt-get -y install apt-transport-https \
      ca-certificates \
      curl \
      gnupg2 \
      groovy \
      jq \
      cowsay \
      software-properties-common \
      python3-venv \
      python3-pip

# Install Docker
RUN curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg > /tmp/dkey; apt-key add /tmp/dkey && \
add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
    $(lsb_release -cs) \
    stable" && \
apt-get update && \
apt-get -y install docker-ce
RUN apt-get install -y docker-ce
RUN usermod -a -G docker jenkins

# Drop back to Jenkins user once done
USER jenkins

# # Pre-install the plugins we want to bake in
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# # Set our username and password - we wouldn't do this in PRODUCTION!!
ENV JENKINS_USER academy
ENV JENKINS_PASS academy

# Create ssh-key that Jenkins can use for git
RUN ssh-keygen -f ~/.ssh/id_rsa -t rsa -N ''

# # allows to skip Jenkins setup wizard
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

# # Jenkins runs all groovy files from init.groovy.d dir, load bootstrap groovy script
COPY bootstrap.groovy /usr/share/jenkins/ref/init.groovy.d/

# Bake in a job to scan for seed jobs
# https://github.com/tknerr/jenkins-pipes-infra/blob/master/Dockerfile
RUN mkdir -p /usr/share/jenkins/ref/jobs
COPY bootstrap_jobs/ /usr/share/jenkins/ref/jobs/
# Disabled for now
# COPY seed_jobs/ /seed_jobs/
COPY scriptApproval.xml.override /usr/share/jenkins/ref/scriptApproval.xml.override

