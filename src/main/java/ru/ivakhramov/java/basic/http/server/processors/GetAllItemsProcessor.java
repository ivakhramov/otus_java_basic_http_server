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
import java.util.List;

public class GetAllItemsProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    public GetAllItemsProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        List<Item> items = itemsRepository.getItems();
        Gson gson = new Gson();
        String itemsJson = gson.toJson(items);

        String response = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "\r\n" +
                itemsJson;
        logger.info(response);
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
