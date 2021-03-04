package com.nickzim.orderparser.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickzim.orderparser.model.InputOrder;
import com.nickzim.orderparser.model.OutputOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class JsonParser implements Parser {

    @Override
    public void handle(Path path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        AtomicLong counter = new AtomicLong(0);

        Files.lines(path, StandardCharsets.UTF_8)
                .map(str -> {
                    InputOrder order;
                    try {
                        order = objectMapper.readValue(str,InputOrder.class);
                        order.setMessage("OK");
                        return order;
                    } catch (IOException exc) {
                        order = new InputOrder();
                        order.setMessage(exc.getClass().getSimpleName() + " " + exc.getMessage());
                    }
                    return order;
                })
                .map(order -> new OutputOrder(order.getOrderId(),order.getAmount(),order.getCurrency(),
                                              order.getComment(),path.toString(),counter.incrementAndGet(),order.getMessage()))
                .forEach(System.out::println);

    }

}
