# Jenkins Lab 3

Prerequisites: 
- [docker-compose](https://docs.docker.com/compose/install/)

## 3.1 Seed Jobs

We never want to create jobs manually. If our server goes down, we have no way of replicating the jobs again without a backup, and additionally we don't have any control of what jobs other people create - after all a CI/CD server is highly priviledged and often has access to credentials, deployment clusters etc. 

This is where seed jobs come in. Seed jobs are normal Jenkins Jobs, but they have one specific purpose - to build other jobs.

Create a new job in the console, but this time make it of type "freestyle project" rather than "pipeline". Name the Job "Seed Job".
In the job configuration screen under `Build`, add an additional step called `Process Job DSLs`
Select `Use the provided DSL script` option.
Write your DSL in the textbox that is now available. The following example will create a job named "demo" with a single task that prints a message.

```
job('demo') {
    steps {
        shell('echo Hello World!')
    }
}
```

Run this job

If you return to the home page you will notice a new job called 'demo' appearing in the Dashboard.
You can see all the possible configurations for seed jobs [here](https://jenkinsci.github.io/job-dsl-plugin/)

## 3.2 Create a new Seed Job

Create a new seed job which automatically creates and populates a new job with the pipeline you created in 2.3. [Here](https://jenkinsci.github.io/job-dsl-plugin/#path/pipelineJob) you can find the syntax of writing seed jobs with jobDSL.
An example of the original job (not the seed job) can be found below:

```
pipeline {
    agent any
    stages {
        stage('Clone sources') {
            steps {
                git branch: 'master', credentialsId: 'git', url: 'git@gitlab.platform-engineering.com:learnwithus/jenkins-training.git'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build ./hello-world -t hello_world_test'
            }
        }
        stage('Docker Test') {
            steps {
                sh 'docker run hello_world_test cat hello_world.txt'
            }
        }
    }
}
```

If you get stuck you may find the following resource helpful:
[Example seed job for pipelineJob](https://souravatta.medium.com/create-jenkins-seedjob-using-job-dsl-script-ac337afd9986#:~:text=pipelineJob(%22greetingJob%22)%20%7B,%7D%0A%20%20%20%20%20%20%7D%0A%20%20%7D)
