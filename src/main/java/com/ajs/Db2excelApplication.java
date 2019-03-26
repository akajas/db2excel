package com.ajs;

import com.ajs.thread.BookThread;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class Db2excelApplication implements CommandLineRunner {

    @Resource
    private BookThread bookThread;

    public static void main(String[] args) {
        SpringApplication.run(Db2excelApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        Thread thBookThread = new Thread(bookThread);
        thBookThread.start();
    }
}
