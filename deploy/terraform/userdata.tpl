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

#install docker compose
sudo apt install docker-compose -y

#access user root
sudo su

#create project folder
mkdir ~/project
cd ~/project

#clone your project from git repository
git clone https://github.com/athirsonsilva/next-spring
cd next-spring
git checkout main
git pull
cd deploy/docker

#run your docker compose file
docker-compose -f docker-compose.yml up -d
