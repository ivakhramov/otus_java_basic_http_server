package ru.ivakhramov.java.basic.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;
    private Dispatcher dispatcher;
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    public RequestHandler(Socket socket, Dispatcher dispatcher) {
        this.socket = socket;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try (socket) {
            byte[] buffer = new byte[8192];
            int n = socket.getInputStream().read(buffer);
            String rawRequest = new String(buffer, 0, n);
            HttpRequest request = new HttpRequest(rawRequest);
            request.info(true);
            dispatcher.execute(request, socket.getOutputStream());
        } catch (IOException e) {
            logger.error("Ошибка: " + e.getStackTrace());
        }
    }
}
