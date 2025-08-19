//
// Job Parameters
//
String repoName = "jenkins-training"
String jobName = "hello-world"
String displayString = "Hello World!"


pipelineJob(jobName) {
    displayName(displayString)

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url "git@gitlab.platform-engineering.com:learnwithus/${repoName}.git"
                        credentials('fake-credentials')
                    }
                    // Specify the branches to examine for changes and to build, comma separated.
                    branches("master")
                }
            }
            scriptPath('hello-world/Jenkinsfile.hello_world')
        }
    }
}
