package com.belprime.testTask;

import com.belprime.testTask.logic.PageExtractor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.belprime.testTask.util.Constants.CDL;

public class ApplicationRunner {

    private ApplicationRunner() {

        CountDownLatch cdl1 = new CountDownLatch(CDL);
        CountDownLatch cdl2 = new CountDownLatch(CDL);
//        CountDownLatch cdl3 = new CountDownLatch(CDL);
//        CountDownLatch cdl4 = new CountDownLatch(CDL);

        ExecutorService executor;
        executor = Executors.newFixedThreadPool(2);

        System.out.println("Запуск потоков");
        executor.execute(new PageExtractor("belprime", cdl1));
        executor.execute(new PageExtractor("google", cdl2));

        try {
            cdl1.await();
            cdl2.await();
//            cdl3.await();
//            cdl4.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        System.out.println("Завершение потоков");
    }

    public static void main(String args[]) {
        new ApplicationRunner();
        System.out.println("Завершение потока main()");
    }
}