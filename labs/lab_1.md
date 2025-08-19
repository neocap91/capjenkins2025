# Jenkins Lab 1

Prerequisites: 
- [docker-compose](https://docs.docker.com/compose/install/)

## 1.1 Getting started

This repo contains a fully operational Jenkins instance configuration, managed by docker-compose. To get the Jenkins instance up and running, enter the root of this directory and run `docker-compose up -d`. To follow log output you can run `docker-compose logs -f`

(Once done, you can clear everything down with `docker-compose down`.)

Once up and running, you should be able to access the Jenkins instance at the following url: http://localhost:8780. 
The instance has been pre-configured with a username and password, which is `academy` for both. 

Once logged in you will see a "Hello World" job.
![Dashboard](images/dashboard.png?raw=true "Dashboard")

You can run the job by clicking on the job name and then `Build now`. If you run the job you will notice an entry in the Build History appear with a red dot. On the Dashboard the job will also show a red dot. This means the job has failed.

## 1.2 Running a job

You can run the job by clicking on the job name and then `Build now`. If you run the job you will notice an entry in the Build History appear with a red dot. On the Dashboard the job will also show a red dot. This means the job has failed.

![Build History](images/red-dot.png?raw=true "Build History")

You can explore the details of the failed job by clicking on the job ID (in this case #1). Here you will see some information regarding the job such as the user which ran it. You will also have the option to view Console Output. 

![Console Output](images/console-output.png?raw=true "Console Output")

The output tells us that we do not have access to the repo we are trying to access in the job. 

Click on the job name again and then go to `configure` to view the job configuration. Here we can see various config options such as parameter options, polling and trigger options. We can also see the repository that the job is pulling from and the credentials it is using. 

## 1.3 Adding credentials

We will now add functioning credentials to the job. 

Since the git URL of the job happens to be the same as this repo, we know that we can use the credentials we used to clone this repo. 
Go back to the Dashboard and then `Credentials` > `System` > `Global credentials (unrestricted)` > `Add Credentials`.

We will now add our local private SSH key by picking `SSH Username with private key` and entering our own username and private key.

![Credentials](images/enter-credentials.png?raw=true "Credentials")

Once you save these you will see them appear alongside `fake-credentials`, which is the ID of the previous credentials.
We now have our SSH key available to all jobs on Jenkins, but we still need to redirect the job to use the correct credentials. 

Go back to `configure` the `Hello World!` job and pick your new credentials which will have appeared in the dropdown menu. Save and Build the job again.

When you view the logs now, you will notice that the job has successfully outputted `Hello World!`