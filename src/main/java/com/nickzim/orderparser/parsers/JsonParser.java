package com.nickzim.orderparser.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickzim.orderparser.model.InputOrder;
import com.nickzim.orderparser.model.OutputOrder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class JsonParser implements Parser {

    Path path;
    AtomicLong counter = new AtomicLong(0);

    @Override
    public void handle(Path path) throws IOException {
        this.path = path;

        ExecutorService service = Executors.newFixedThreadPool(2);

        try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("Windows-1251"))){

            String line;
            Future<InputOrder> parseTask;
            Future<OutputOrder> convertTask;

            parseTask = service.submit(() -> parse(br.readLine()));
            InputOrder order = parseTask.get();

            while ((line = br.readLine()) != null){
                final String str = line;

                final InputOrder parseOrder = order;
                convertTask = service.submit(() -> convert(parseOrder));

                parseTask = service.submit(() -> parse(str));
                order = parseTask.get();

                System.out.println(convertTask.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return;
        }

        counter.set(0);
        service.shutdown();

    }

    private InputOrder parse(String line){
        InputOrder order;
        try {
            order = new ObjectMapper().readValue(line,InputOrder.class);
            order.setMessage("OK");
            return order;
        } catch (IOException exc) {
            order = new InputOrder();
            order.setMessage(exc.getClass().getSimpleName() + " " + exc.getMessage());
        }
        return order;
    }

    private OutputOrder convert(InputOrder order) {
        return new OutputOrder(order.getOrderId(),order.getAmount(),order.getCurrency(),
                order.getComment(),path.toString(),counter.incrementAndGet(),order.getMessage());
    }

}
