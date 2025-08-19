/*
Groovy script for bootstrapping Jenkins configuration
*/

import jenkins.model.*
import hudson.model.*
import hudson.security.*
import hudson.security.csrf.DefaultCrumbIssuer
import jenkins.model.Jenkins
import jenkins.security.s2m.AdminWhitelistRule
import jenkins.security.QueueItemAuthenticatorConfiguration
import org.jenkinsci.plugins.authorizeproject.GlobalQueueItemAuthenticator
import org.jenkinsci.plugins.authorizeproject.strategy.*
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl
import org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.CredentialsScope
import jenkins.model.Jenkins
import hudson.util.Secret
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey
import com.cloudbees.plugins.credentials.impl.CertificateCredentialsImpl
import com.cloudbees.plugins.credentials.SecretBytes

def createAndSetDefaultUser(jenkins, env){
    if(!(jenkins.getSecurityRealm() instanceof HudsonPrivateSecurityRealm)){
        jenkins.setSecurityRealm(new HudsonPrivateSecurityRealm(false))
    }

    if(!(jenkins.getAuthorizationStrategy() instanceof GlobalMatrixAuthorizationStrategy)){
        jenkins.setAuthorizationStrategy(new GlobalMatrixAuthorizationStrategy())
    }

    // create new Jenkins user account
    // username & password from environment variables
    def user = jenkins.getSecurityRealm().createAccount(env.JENKINS_USER, env.JENKINS_PASS)
    user.save()

    // Give admin access to our admin user
    jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, env.JENKINS_USER)
    jenkins.getAuthorizationStrategy().add(org.jenkinsci.plugins.workflow.cps.replay.ReplayAction.REPLAY,env.JENKINS_USER)

    // Give read-only to everyone else logged-in.
    jenkins.getAuthorizationStrategy().add(Jenkins.READ,"authenticated")
    jenkins.getAuthorizationStrategy().add(org.jenkinsci.plugins.workflow.cps.replay.ReplayAction.REPLAY,"authenticated")
    jenkins.save()
}

def enableCSRF(jenkins){
    /* Taken from: https://wiki.jenkins.io/display/JENKINS/CSRF+Protection */
    jenkins.setCrumbIssuer(new DefaultCrumbIssuer(true))
    jenkins.save()
}

def enableSlaveAccessControl(jenkins){
    /* Taken from: https://wiki.jenkins.io/display/JENKINS/Slave+To+Master+Access+Control */
    jenkins.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)
}

def configureDefaultProjectAuth(){
    GlobalQueueItemAuthenticator auth = new GlobalQueueItemAuthenticator(new TriggeringUsersAuthorizationStrategy())
    QueueItemAuthenticatorConfiguration.get().authenticators.add(auth)
}

def createSshKeyConfig(store, domain, String keyfile="/var/jenkins_home/.ssh/id_rsa"){
    /*
        Details here: http://tdongsi.github.io/blog/2017/12/30/groovy-hook-script-and-jenkins-configuration-as-code/
    */
    def privateKey = new BasicSSHUserPrivateKey(
            CredentialsScope.GLOBAL,
            "jenkins_ssh_key",
            "git",
            new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(keyfile),
            "",
            ""
    )
    store.addCredentials(domain, privateKey)
}

def createUserPassConfig(store, domain){

    def gitlabAccount = new UsernamePasswordCredentialsImpl(
        CredentialsScope.GLOBAL, "fake-credentials", "Gitlab Jenkins Lab Repo",
        "academy",
        "password"
    )

    store.addCredentials(domain, gitlabAccount)
}

def main(){
    def env = System.getenv()
    def jenkins = Jenkins.getInstance()
    def domain = Domain.global()
    def store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

    System.setProperty('org.apache.commons.jelly.tags.fmt.timeZone', 'Europe/London')
    System.setProperty('user.timezone', 'Europe/London')

    configureDefaultProjectAuth()
    createAndSetDefaultUser(jenkins, env)
    enableCSRF(jenkins)
    enableSlaveAccessControl(jenkins)
    // createSshKeyConfig(store, domain)
    createUserPassConfig(store, domain)
}

main()
