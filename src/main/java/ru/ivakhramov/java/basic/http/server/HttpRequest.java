package ru.ivakhramov.java.basic.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String rawRequest;
    private HttpMethod method;
    private String uri;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String body;
    private Exception exception;
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getUri() {
        return uri;
    }

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getBody() {
        return body;
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parse();
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public boolean containsHeader(String key) {
        return headers.containsKey(key);
    }

    private void parse() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        uri = rawRequest.substring(startIndex + 1, endIndex);
        method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        parameters = new HashMap<>();
        headers = new HashMap<>();

        String[] requestPart = rawRequest.split("\r\n\r\n");
        String headerSection = requestPart[0];

        if (requestPart.length > 1) {
            body = requestPart[1];
        }

        String[] headerLines = headerSection.split("\r\n");
        for (int i = 1; i < headerLines.length; i++) {
            String[] headerKeyValue = headerLines[i].split(": ", 2);
            if (headerKeyValue.length == 2) {
                headers.put(headerKeyValue[0], headerKeyValue[1]);
            }
        }

        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            uri = elements[0];
            String[] keysValues = elements[1].split("[&]");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                parameters.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public void info(boolean debug) {
        if (debug) {
            logger.debug(rawRequest);
        }
        logger.info("Method: " + method);
        logger.info("URI: " + uri);
        logger.info("Parameters: " + parameters);
        logger.info("Headers: " + headers);
        logger.info("Body: " + body);
    }
}
