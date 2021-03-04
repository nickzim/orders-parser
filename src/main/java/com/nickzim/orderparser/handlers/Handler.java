package com.nickzim.orderparser.handlers;

import java.io.IOException;
import java.nio.file.Path;

public interface Handler {

    void handle(Path path) throws IOException;

}
