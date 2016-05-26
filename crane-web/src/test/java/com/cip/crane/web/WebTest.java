package com.cip.crane.web;

import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.webapp.WebAppContext;
import org.unidal.test.jetty.JettyServer;

public class WebTest extends JettyServer {

    @Before
    public void before() throws Exception {
        System.setProperty("devMode", "true");
        super.startServer();
    }

    @Override
    protected String getContextPath() {
        return "/";
    }

    @Override
    protected int getServerPort() {
        return 2281;
    }

    @Override
    protected void postConfigure(WebAppContext context) {
    }

    @Test
    public void startWebApp() throws Exception {
        // open the page in the default browser
        display("/");
        waitForAnyKey();
    }
}

/*
#!/usr/bin/expect
set timeout 30
set ip[lindex $argv 0]
spawn ssh-p58422 betauser@$ip
expect{
        "*?assword:*"{
        send"user4beta\r"

        send"alias cls='clear'\r"
        send"alias dir='ls -la'\r"
        send"alias nobody='sudo -u nobody'\r"

        send"cd /data/appdatas/liger\r"

        send"sudo vim /etc/sudoers\ro\x1b^ibetauser    ALL=(ALL)  NOPASSWD: ALL\r\x1b:wq!\r"

        interact
        }
        "*(yes/no)*"{
        sleep 1
        send"yes\r"
        exp_continue
        }
        }
*/