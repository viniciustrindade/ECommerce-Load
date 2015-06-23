package com.appdynamics.demo;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by aleftik on 6/18/14.
 */
public abstract class StaticRequestLoadTest implements Runnable {
    static final Logger logger = Logger.getLogger(StaticRequestLoadTest.class.getName());
    private String host = null;
    private int callDelay = 0;
    private WebDriver driver;
    private int port = 80;
    private int angularPort = 8080;


    public StaticRequestLoadTest(String host, int port, int angularPort, int callDelay) {
        this.host = host;
        this.port = port;
        this.callDelay = callDelay;
        this.angularPort = angularPort;
    }

    public void init() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.cache.disk.enable", false);
        profile.setPreference("browser.cache.memory.enable", false);
        profile.setPreference("browser.cache.offline.enable", false);
        profile.setPreference("network.http.use-cache", false);
        profile.setPreference("dom.enable_resource_timing", true);
        driver = new FirefoxDriver(profile);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
    }

    public void run() {
        try {
            init();
            executeTest();

        } finally {
            destroy();
        }

    }

    protected void executeTest() {
        for (int i = 0; i < getUrls().length; i++) {
            fetchUrl(getUrls()[i]);
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.currentThread().sleep(callDelay);
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
    }

    private void fetchUrl(String uri) {
        if (driver != null) {
            String url = "http://" + host + uri;
            logger.info("fetching :" + url);
            driver.get(url);
        } else {
            logger.info("*** Web driver is null nothing to fetch ***");
        }


    }

    public void destroy() {
        if (driver != null) {
            driver.quit();
            driver.close();
        }

    }

    protected WebDriver getDriver() {
        return this.driver;
    }

    protected String getHost() {
        return this.host;
    }


    protected int getPort() {
        return this.port;
    }

    protected int getAngularPort() {
        return this.angularPort;
    }

    abstract String[] getUrls();
}
