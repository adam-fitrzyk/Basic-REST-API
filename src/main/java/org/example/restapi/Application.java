package org.example.restapi;

public class Application {

    public static void main(String[] args) {
        var server = new Server(6868);
        server.run();
    }

}
