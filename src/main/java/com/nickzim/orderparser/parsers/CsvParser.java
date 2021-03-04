package com.nickzim.orderparser.parsers;

import com.nickzim.orderparser.model.InputOrder;
import com.nickzim.orderparser.model.OutputOrder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CsvParser implements Parser {

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
        String[] args = line.split(";");
        try {
            return new InputOrder(Long.parseLong(args[0]), Double.parseDouble(args[1]), args[2], args[3], "OK");
        } catch (NumberFormatException exc){
            InputOrder order = new InputOrder();
            order.setMessage(exc.getClass().getSimpleName() + " " + exc.getMessage());
            return order;
        }
    }

    private OutputOrder convert(InputOrder order) {
        return new OutputOrder(order.getOrderId(),order.getAmount(),order.getCurrency(),
                order.getComment(),path.toString(),counter.incrementAndGet(),order.getMessage());
    }

}
