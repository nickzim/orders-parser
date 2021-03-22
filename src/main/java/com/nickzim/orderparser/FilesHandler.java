package com.nickzim.orderparser;

import com.nickzim.orderparser.handlers.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FilesHandler {

    private final Map<String, Handler> handlers;

    public void printFromFiles(String... files){

        for (String it: files){

            Path file = Paths.get(it);
            String extension = it.substring(it.indexOf('.') + 1);

            switch (extension.toLowerCase()){

                case "json":{
                    try {
                        handlers.get("jsonHandler").handle(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "csv":{
                    try {
                        handlers.get("csvHandler").handle(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }
        }
    }
}
