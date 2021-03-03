package com.nickzim.parsers;

import com.nickzim.model.InputOrder;
import com.nickzim.model.OutputOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CsvParser implements Parser {

    @Override
    public void convert(Path path) throws IOException {

        AtomicLong counter = new AtomicLong(0);

        Files.lines(path, Charset.forName("Windows-1251"))
                .map(str ->{
                    String[] args = str.split(";");
                    try {
                        return new InputOrder(Long.parseLong(args[0]), Double.parseDouble(args[1]), args[2], args[3]);
                    } catch (NumberFormatException exc){
                        return new InputOrder();
                    }
                })
                .map(order -> new OutputOrder(order.getOrderId(),order.getAmount(),order.getCurrency(),
                        order.getComment(),path.toString(),counter.incrementAndGet(),"OK"))
                .forEach(System.out::println);
    }
}
