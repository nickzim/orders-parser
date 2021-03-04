package com.nickzim.orderparser;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.nickzim.orderparser")
public class Main {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
        OutputWriter writer = ctx.getBean(OutputWriter.class);
        writer.writeFromFiles(args);
    }
}
