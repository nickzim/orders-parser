package com.nickzim.orderparser.parsers;

import java.io.IOException;
import java.nio.file.Path;

public interface Parser {

    void handle(Path path) throws IOException;

}
