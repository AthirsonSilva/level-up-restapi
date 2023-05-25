#!/bin/bash

# Build the project
mvn clean install

# Run the project
docker build .
