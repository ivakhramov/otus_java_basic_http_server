package ru.ivakhramov.java.basic.http.server;

public class Application {

    public static void main(String[] args) {
        new HttpServer(8188).start();
    }
}
