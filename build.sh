#!/bin/bash

# Message
echo "Building the project..."

# Build the project
mvn clean install

# Run the project
docker build .
