package com.appdynamics.demo;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class LoadRunner {
    private static int numOfUsers;
    private static int rampUpTime;
    private static int waitTime = 100;
    private static int timeBetweenRuns = 3 * 1000;
    private static int port;
    private static int angularPort;
    private static String host = "pm-demo.appdynamics.com";
    private static String angularHost = "angular";
    private static List<User> userList = new ArrayList<>();

    ScheduledExecutorService pool;

    public LoadRunner() {

    }

    private void init() {
        pool = Executors.newScheduledThreadPool(numOfUsers);
    }

    private void run() {
        while (true) {
            for (int i = 0; i < numOfUsers; i++) {
                //pool.schedule(new ECommerceCheckout(host, angularHost, port, angularPort, waitTime,userList), rampUpTime, TimeUnit.MILLISECONDS);
                pool.schedule(new ECommerceAngularCheckout(host, angularHost, port, angularPort, waitTime,userList), rampUpTime, TimeUnit.MILLISECONDS);
            }
            sleep();
        }

    }

    private void sleep() {
        try {
            Thread.currentThread().sleep(timeBetweenRuns);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        parseArgs(args);
        userList = getUserInformation();
        LoadRunner runner = new LoadRunner();
        runner.init();
        runner.run();
    }

    private static List<User> getUserInformation() {
        Client client = Client.create();

        WebResource webResource = client
                .resource("http://" + host + ":" +port + "/appdynamicspilot/rest/json/user/all");

        String response = webResource.accept("application/json")
                .get(String.class);
        Gson gson = new Gson();
        TypeToken<List<User>> token = new TypeToken<List<User>>() {
        };
        List<User> userList = gson.fromJson(response, token.getType());
        return userList;

    }


    private static void parseArgs(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: numberOfUsers rampUpTime timeBetweenRuns baseUrl angularUrl port angularPort [waitTime]");
        }
        numOfUsers = Integer.parseInt(args[0]);
        rampUpTime = Integer.parseInt(args[1]);
        timeBetweenRuns = Integer.parseInt(args[2]);
        host = args[3];
        angularHost = args[4];
        port = Integer.parseInt(args[5]);
        angularPort = Integer.parseInt(args[6]);

        if (args.length == 8) {
            waitTime = Integer.parseInt(args[7]);
        }

    }
}
