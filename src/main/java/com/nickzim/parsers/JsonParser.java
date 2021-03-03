package com.nickzim.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickzim.model.InputOrder;
import com.nickzim.model.OutputOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class JsonParser implements Parser {

    @Override
    public void convert(Path path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        AtomicLong counter = new AtomicLong(0);

        Files.lines(path, StandardCharsets.UTF_8)
                .map(str -> {
                    try {
                        return objectMapper.readValue(str,InputOrder.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new InputOrder();
                })
                .map(order -> new OutputOrder(order.getOrderId(),order.getAmount(),order.getCurrency(),
                                              order.getComment(),path.toString(),counter.incrementAndGet(),"OK"))
                .forEach(System.out::println);

    }

}
