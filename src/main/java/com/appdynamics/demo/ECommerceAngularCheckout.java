package com.appdynamics.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by aleftik on 6/18/14.
 */
public class ECommerceAngularCheckout extends ECommerceAngularSession {

    static private final Random randomGen = new Random();

    public ECommerceAngularCheckout(String host, String angularHost, int port, int angularPort, int callDelay) {
        super(host, angularHost, port, angularPort, callDelay);
    }

    @Override
    void performLoad() {
        WebDriver angularDriver = getDriver();
        angularDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        angularDriver.get(getScheme() + getAngularHost() + ':' + getAngularPort() + getAngularProductsUrl());

        try {
            //Angular
            List<WebElement> ids = angularDriver.findElements(By.id("prodid"));
            int angularNumBooks = randomGen.nextInt(3);
            for (int i = 0; i < angularNumBooks + 1; i++) {
                int bookNumber = 1 + randomGen.nextInt(ids.size() - 1);
                WebElement angularAddToCart = angularDriver.findElement(By.xpath("//div[@id='prodid' and text()=" + bookNumber + "]")).findElement(By.xpath("./following-sibling::button"));
                logger.info("Angular - Selected book # " + bookNumber);
                try {
                    Thread.currentThread().sleep(500);
                } catch (Exception ex) {
                }
                angularAddToCart.click();
                logger.info("Angular - Items added to Cart");
            }

            WebDriver angularCheckoutDriver = getDriver();
            angularCheckoutDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            angularCheckoutDriver.get(getScheme() + getAngularHost() + ':' + getAngularPort() + getAngularCartUrl());

            WebElement angularSubmit = angularCheckoutDriver.findElement(By.id("btnCheckout"));
            try {
                Thread.currentThread().sleep(500);
            } catch (Exception ex) {
            }
            angularSubmit.click();
            logger.info("Angular - Checkout Cart");

        } catch (Exception ex) {
            logger.warning(ex.getMessage());
        }
    }


    @Override
    User getUserInfo() {
        List<User> users = getUserInformation();
        logger.info("userInfo Size : " + users.size());
        if (users != null && users.size() > 0) {
            Random generator = new Random();
            int index = generator.nextInt(users.size());
            index = (index == users.size()) ? index - 1 : index;
            User user = users.get(index);
            logger.info("User Name : " + user.getEmail());
            logger.info("Password : " + user.getPassword());
            return user;
        }
        return null;
    }

    @Override
    String getAngularLoginUrl() {
        return "/angular/#/login";
    }

    @Override
    String getAngularProductsUrl() {
        return "/angular/#/";
    }

    String getAngularCartUrl() {
        return "/angular/#/cart";
    }

    String getScheme() {
        return "http://";
    }
}
