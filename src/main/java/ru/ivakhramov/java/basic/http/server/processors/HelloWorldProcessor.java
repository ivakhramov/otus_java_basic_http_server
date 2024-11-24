package ru.ivakhramov.java.basic.http.server.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ivakhramov.java.basic.http.server.HttpRequest;
import ru.ivakhramov.java.basic.http.server.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HelloWorldProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        String response = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>Hello World!!!</h1><table><tr><td>1</td><td>2</td></tr></table></body></html>";
        logger.info(response);
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
