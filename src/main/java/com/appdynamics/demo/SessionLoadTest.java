package com.appdynamics.demo;

/**
 * Created by aleftik on 6/18/14.
 */
public abstract class SessionLoadTest extends StaticRequestLoadTest {


     public SessionLoadTest(String host, int port, int callDelay) {
         super(host, port, callDelay);
     }

    abstract void login();
    abstract void logout();
    abstract void performLoad();

    @Override
    protected void executeTest() {
        try {
            login();
            performLoad();
            logout();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            destroy();
        }
    }

    public String [] getUrls() {
        return null;
    }
}