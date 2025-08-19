# Jenkins Lab Environment

This repository contains configuration files needed to build and run a docker image with Jenkins pre-built.

## Getting started
Run `docker-compose up -d` to start up the Jenkins instance. To follow log output you can run `docker-compose logs -f`

Once done, you can clear everything down with `docker-compose down`

## Accessing the Jenkins instance
Once up and running, you should be able to access the Jenkins instance at the following url: http://localhost:8780. 
The Jenkins instance is pre-configured with a "Hello World" job.
This repository can be used for any experiments you wish to do with Jenkinsfiles.

## Issues, Troubleshooting
This should all work with little issue, but if this is not the case please reach out to Pi Unnerup.
