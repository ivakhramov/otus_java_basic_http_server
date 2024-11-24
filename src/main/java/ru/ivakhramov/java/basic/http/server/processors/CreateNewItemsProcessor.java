package ru.ivakhramov.java.basic.http.server.processors;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ivakhramov.java.basic.http.server.HttpRequest;
import ru.ivakhramov.java.basic.http.server.HttpServer;
import ru.ivakhramov.java.basic.http.server.app.Item;
import ru.ivakhramov.java.basic.http.server.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CreateNewItemsProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    public CreateNewItemsProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Item item = itemsRepository.save(gson.fromJson(request.getBody(), Item.class));

        String response = "" +
                "HTTP/1.1 201 Created\r\n" +
                "Content-Type: application/json\r\n" +
                "\r\n" +
                gson.toJson(item);
        logger.info(response);
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
