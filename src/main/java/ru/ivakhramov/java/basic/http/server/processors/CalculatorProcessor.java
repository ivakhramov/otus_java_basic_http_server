package ru.ivakhramov.java.basic.http.server.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ivakhramov.java.basic.http.server.BadRequestException;
import ru.ivakhramov.java.basic.http.server.HttpRequest;
import ru.ivakhramov.java.basic.http.server.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        if (!request.containsParameter("a")) {
            logger.error("Parameter 'a' is missing");
            throw new BadRequestException("Parameter 'a' is missing");
        }
        if (!request.containsParameter("b")) {
            logger.error("Parameter 'b' is missing");
            throw new BadRequestException("Parameter 'b' is missing");
        }
        int a, b;
        try {
            a = Integer.parseInt(request.getParameter("a"));
        } catch (NumberFormatException e) {
            logger.error("Parameter 'a' has incorrect type");
            throw new BadRequestException("Parameter 'a' has incorrect type");
        }
        try {
            b = Integer.parseInt(request.getParameter("b"));
        } catch (NumberFormatException e) {
            logger.error("Parameter 'b' has incorrect type");
            throw new BadRequestException("Parameter 'b' has incorrect type");
        }

        String math = a + " + " + b + " = " + (a + b);
        String response = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>" + math + "</h1></body></html>";
        logger.info(response);
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
