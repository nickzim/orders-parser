package com.nickzim;

import com.nickzim.parsers.CsvParser;
import com.nickzim.parsers.JsonParser;
import com.nickzim.parsers.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OutputWriter {

    private final Map<String,Parser> parsers;

    public void writeFromFiles(String... files){

        for (String it: files){

            Path file = Paths.get(it);
            String extension = file.toString().substring(file.toString().indexOf('.') + 1);

            switch (extension.toLowerCase()){
                
                case "json":{
                    try {
                        parsers.get("jsonParser").convert(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "csv":{
                    try {
                        parsers.get("csvParser").convert(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
