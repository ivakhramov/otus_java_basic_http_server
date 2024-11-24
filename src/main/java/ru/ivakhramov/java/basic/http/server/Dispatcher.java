package ru.ivakhramov.java.basic.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ivakhramov.java.basic.http.server.app.ItemsRepository;
import ru.ivakhramov.java.basic.http.server.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private Map<String, RequestProcessor> processors;
    private RequestProcessor defaultNotFoundProcessor;
    private RequestProcessor defaultInternalServerErrorProcessor;
    private RequestProcessor defaultBadRequestProcessor;
    private RequestProcessor defaultMethodNotAllowedProcessor;
    private ItemsRepository itemsRepository;
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    public Dispatcher() {
        this.itemsRepository = new ItemsRepository();
        this.processors = new HashMap<>();
        this.processors.put("GET /", new HelloWorldProcessor());
        this.processors.put("GET /calculator", new CalculatorProcessor());
        this.processors.put("GET /items", new GetAllItemsProcessor(itemsRepository));
        this.processors.put("POST /items", new CreateNewItemsProcessor(itemsRepository));
        this.defaultNotFoundProcessor = new DefaultNotFoundProcessor();
        this.defaultInternalServerErrorProcessor = new DefaultInternalServerErrorProcessor();
        this.defaultBadRequestProcessor = new DefaultBadRequestProcessor();
        this.defaultMethodNotAllowedProcessor = new DefaultMethodNotAllowedProcessor();
    }

    public void execute(HttpRequest request, OutputStream out) throws IOException {
        try {
            if (!processors.containsKey(request.getRoutingKey())) {
                if(processors.keySet().stream().allMatch(key -> key.split(" ")[1].equals(request.getUri()))) {
                    defaultMethodNotAllowedProcessor.execute(request, out);
                } else {
                    defaultNotFoundProcessor.execute(request, out);
                }
                return;
            }
            processors.get(request.getRoutingKey()).execute(request, out);
        } catch (BadRequestException e) {
            request.setException(e);
            defaultBadRequestProcessor.execute(request, out);
            logger.error("Ошибка: " + e.getStackTrace());
        } catch (Exception e) {
            e.printStackTrace();
            defaultInternalServerErrorProcessor.execute(request, out);
            logger.error("Ошибка: " + e.getStackTrace());
        }
    }
}
