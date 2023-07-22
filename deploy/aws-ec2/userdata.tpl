#!/bin/bash
# install docker
sudo apt-get update -y &&
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common &&
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - &&
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" &&
sudo apt-get update -y &&
sudo sudo apt-get install docker-ce docker-ce-cli containerd.io -y &&
sudo usermod -aG docker ubuntu
sudo systemctl enable docker.service
sudo systemctl enable containerd.service

# install docker compose
sudo apt install docker-compose -y

# access user root
sudo su

# create project directory
mkdir /home/ubuntu/project
cd /home/ubuntu/project

# clone your project from git repository
git clone https://github.com/athirsonsilva/spring-reactive
cd spring-reactive

# run your docker compose file
docker-compose up -d
