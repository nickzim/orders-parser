package com.nickzim.orderparser.parsers;

import com.nickzim.orderparser.model.InputOrder;
import com.nickzim.orderparser.model.OutputOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CsvParser implements Parser {
    

    @Override
    public void handle(Path path) throws IOException {

        AtomicLong counter = new AtomicLong(0);

        Files.lines(path, Charset.forName("Windows-1251"))
                .parallel()
                .map(str ->{
                    String[] args = str.split(";");
                    try {
                        return new InputOrder(Long.parseLong(args[0]), Double.parseDouble(args[1]), args[2], args[3], "OK");
                    } catch (NumberFormatException exc){
                        InputOrder order = new InputOrder();
                        order.setMessage(exc.getClass().getSimpleName() + " " + exc.getMessage());
                        return order;
                    }
                })
                .map(order -> new OutputOrder(order.getOrderId(),order.getAmount(),order.getCurrency(),
                        order.getComment(),path.toString(),counter.incrementAndGet(),order.getMessage()))
                .forEach(System.out::println);
    }

}
