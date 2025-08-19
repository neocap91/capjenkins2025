# Jenkins Lab 4

Prerequisites: 
- [docker-compose](https://docs.docker.com/compose/install/)

## 4.1 Configuring Jenkins

Jenkins is highly pluggable and highly configurable. So far we have looked mainly at pipelines and Jenkins jobs, but often you will run into requirements which aren't available out of the box. 

Ideally we will be configuring Jenkins automatically such as using a bootstrap.groovy script like the one you can fine in the `/jenkins/` directory of this repo, but it's worth having a look around Jenkins to see our configuration options. Go to `Manage Jenkins`.

![Manage-jenkins](images/manage-jenkins.png?raw=true "Manage Jenkins")

Here we see a long list of configurable options for Jenkins. Depending on your plugins you may see fewer or more menu items here. You should hopefully see the `script console` to run groovy scripts directly on the main Jenkins instance. Try to print the list of existing plugins here with `println(Jenkins.instance.pluginManager.plugins)`

On the management page you will also see useful configuration options such as credentials, security options, users and plugins.

## 4.2 Updating Plugins

Let's have a look into the plugin manager with `Manage Plugins`. We have preloaded our instance with the [list of plugins](jenkins/plugins.txt), but we also have the option to add or update plugins.

Later on in this lab, we will have a look at managing authorization with Jenkins, so lets have a go at updating the `Matrix Authorization Stategy` plugin which appears to be out of date.

![Plugin](images/plugin.png?raw=true "Matrix Plugin")

Tick the plugin, click `Download now and install after restart`, then tick `Restart Jenkins when installation is complete and no jobs are running`. Jenkins will restart and subsequently download the plugin. 

Do you see any security issues in the pluggable nature of Jenkins?

## 4.3 RBAC

We have now uptdated the plugin for authorization and we are ready to start creating some users to use our Jenkins Instance. 
Go to `Manage Jenkins` > `Configure Global Security`. You will see the authotization Matrix, which currently has only one named user, `academy`, which is the user we have been using to do everything in Jenkins so far. As you can see this user has overall admin, which is the most priviledged option.

![Matrix](images/matrix.png?raw=true "Matrix")

We will now create another user. Go to `Manage Jenkins` > `Manage Users` > `Create User`. You can name the user whatever you like, but I have named mine `dev` with password `dev`.

![Dev](images/user.png?raw=true "User")

When you have created the user, sign out of `academy` and sign in as `dev`. You will notice that you can log in but your view is very limited. Why is this? Sign back in as `academy` go back to configure security. We need to add `dev` to our authorization matrix, by clicking `Add user or group...` after which we should see the name of our user appear. Let's give dev overall Read and Build on our Job. Save the configuration.

![Dev](images/dev-read.png?raw=true "User")

Log out of `academy` and back in as `dev`.
Surprisingly you still cannot see any jobs, even though we just gave ourselves overall read. This is because overall read is not equivalent to overall admin. Where overall admin applies to all features of Jenkins and you don't need to tick anything else, overall read does not give you access to jobs. 

Repeat the process, but this time give dev the Job Read option as well. When you log in as `dev`, you will now see the jobs as expected. When you click on one, you will notice that as a developer you have access to build the job, but you can no longer configure it as you could with admin. 

What are some issues with creating users in this way?
What would be a better way of managing users?

## 4.4 Troubleshooting Jenkins

Jenkins has a couple of built in methods of helping you troubleshoot issues. 
- The [Jenkins CLI](https://www.jenkins.io/doc/book/managing/cli/) lets you access Jenkins from a script or shell environment, which can be helpful for any updates or testing or troubleshooting
- The Script console, which is found through the config manager lets you execute groovy scripts directly on the main Jenkins instance
- The System Log, which is found through the config manager lets you view logs and diagnose any immediate jenkins issues

Besides these tools, I find it the most helpful to simply exec onto the instance or agent and have a poke around myself. 
On your local machine run `docker container ls` to find the container ID of the Jenkins instance and then `docker exec -ti {container ID} bash` to exec onto the instance. 

Can you find a list of users?
What about a list of plugins?

Try to create a pipeline with an agent, that does a `sleep 10000`. When you build the job, you will notice another container starting on your machine. Exec into this agent and explore the file system in here. Does it look like what you would expect?

## 4.5 STRETCH: Library

[Jenkins shared libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/) are a way of managing replication across pipelines without duplicating code.
As a stretch goal set up a simple Jenkins Shared Library with a single pipeline in a separate repo, and consume it through a pipeline in Jenkins. 
[This](https://devopscube.com/jenkins-shared-library-tutorial/) is a decent guide to Jenkins Shared Libraries.