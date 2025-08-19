# Jenkins Lab 2

Prerequisites: 
- [docker-compose](https://docs.docker.com/compose/install/)

## 2.1 Setting up a new job

The `Hello World!` job was generated automatically from a seed file. This is the most common way to manage jobs as it allows us to automatically set up and run jobs, however it is possible to add jobs manually. 

In this section we will add a manual pipeline job to see the components that go into setting up a pipeline in Jenkins. 

Go to `New Item` and name your item (or job as most people call it) a name of your choice, e.g. `Test Job`. Choose `Pipeline` as its type and click OK. 

The following page is similar to the `configure` page we saw on our `Hello World!` job, but without any configuration. 

Under `Pipeline` we have to supply a pipeline script, either through manual typing or by clicking on the dropdown and choosing `Pipeline Script from SCM`. `Hello World!` was configured to use a script from Gitlab.

This time we will supply the script manually. 

```
pipeline {
   agent any

   stages {
      stage('This is Stage 1') {
         steps {
            echo 'My First Pipeline!'
         }
      }
   }
}
```

Save the configuration and build the job. View the console output.

![Pipeline-1](images/pipeline-1.png?raw=true "Pipeline-1")

This time we did not get an error on credentials. Why is that?

We can see the [Jenkins Documentation](https://www.jenkins.io/doc/book/pipeline/syntax/) for pipeline syntax.

## 2.2 Jenkins Agents

Jenkins can run jobs directly from the main Jenkins instance such as in the example above. 
However sometimes we want tooling available which is not installed on the main Jenkins instance, in which case we can use agents (in older documentation agents are often referred to as slaves). Agents are separate instances (e.g. docker containers) which can be pulled to execute a particular workload. You can define agents for the whole pipeline or for individual stages of the pipeline.

Create a new job or edit the existing with the following pipeline:

```
pipeline {
    agent none 
    stages {
        stage('Example Build') {
            agent { docker 'maven:3.8.1-adoptopenjdk-11' } 
            steps {
                echo 'Hello, Maven'
                sh 'mvn --version'
            }
        }
        stage('Example Test') {
            agent { docker 'openjdk:8-jre' } 
            steps {
                echo 'Hello, JDK'
                sh 'java -version'
            }
        }
    }
}
```

Run it and see how one agent has maven installed and another java.

## 2.3 Creating a build pipeline

Putting this all together, we are now going to build a pipeline with the following criteria:

The pipeline should have 3 stages: 
- Pull code located [here](https://gitlab.platform-engineering.com/learnwithus/jenkins-training/-/blob/master/hello-world/Dockerfile)
- Build Docker image, e.g.: `docker build ./hello-world -t hello_world_test`
- Test image, e.g.: `docker run hello_world_test cat hello_world.txt`

STRETCH GOAL: parameterise the job so we can choose the name of the image

The pipeline can use any agent or none
The pipeline will use the SSH key defined in lab 1

[How to checkout a repository](https://www.jenkins.io/doc/pipeline/steps/git/)

There are multiple ways of achieving this pipeline, so have fun with it!