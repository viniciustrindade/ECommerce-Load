package com.appdynamics.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by aleftik on 6/18/14.
 */
public abstract class ECommerceAngularSession extends SessionLoadTest {


    public ECommerceAngularSession(String host, int port, int angularPort, int callDelay) {
        super(host, port, angularPort, callDelay);
    }

    @Override
    void login() {
        //Angular
        WebDriver angularDriver = getDriver();
        angularDriver.get(getScheme() + getHost() + ':' + getAngularPort() + getAngularLoginUrl());
        logger.info("Angular Logging into " + getScheme() + getHost() + ':' + getAngularPort() + getAngularLoginUrl());
        try {
            Thread.currentThread().sleep(500);
        } catch (Exception ex) {
        }
        User user = getUserInfo();
        angularDriver.findElement(By.id("username")).sendKeys(user.getEmail());
        angularDriver.findElement(By.id("password")).sendKeys(user.getPassword());
        try {
            Thread.currentThread().sleep(500);
        } catch (Exception ex) {
        }
        angularDriver.findElement(By.id("btnLogin")).click();

    }

    @Override
    void logout() {
        logger.info("Logged out Angular");
    }

    @Override
    abstract void performLoad();

    abstract String getAngularLoginUrl();

    abstract String getAngularProductsUrl();

    abstract User getUserInfo();

    abstract String getScheme();
}
