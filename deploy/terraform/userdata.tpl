#!/bin/bash
# Update the apt package index and install packages to allow apt to use a repository over HTTPS:
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

#	access user root to run docker commands
sudo su

# go to home directory to clone your project
cd /home/ubuntu

# clone your project from git repository
git clone https://github.com/athirsonsilva/next-spring
cd next-spring

# checkout to main branch and pull the latest changes
git checkout main
git pull

# go to docker directory
cd deploy/docker

# run your docker compose file
docker-compose -f docker-compose.yml up -d
