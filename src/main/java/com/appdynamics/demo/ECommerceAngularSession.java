package com.appdynamics.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by aleftik on 6/18/14.
 */
public abstract class ECommerceAngularSession extends SessionLoadTest {


    public ECommerceAngularSession(String host, String angularHost, int port, int angularPort, int callDelay) {
        super(host, angularHost, port, angularPort, callDelay);
    }

    @Override
    void login() {
        //Angular
        try {
            User user = getUserInfo();

            WebDriver angularDriver = getDriver();
            angularDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            angularDriver.get(getScheme() + getAngularHost() + ':' + getAngularPort() + getAngularLoginUrl());
            logger.info("Angular Logging into " + getScheme() + "angular" + ':' + getAngularPort() + getAngularLoginUrl());

            angularDriver.findElement(By.id("username")).sendKeys(user.getEmail());
            angularDriver.findElement(By.id("password")).sendKeys(user.getPassword());
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception ex) {
            }
            angularDriver.findElement(By.id("btnLogin")).click();
        } catch (Exception ex) {
            logger.warning(ex.getMessage());
        }

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
