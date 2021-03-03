package com.nickzim.parsers;

import java.nio.file.Path;

public interface Parser {

    void convert(Path path) throws Exception;

}
