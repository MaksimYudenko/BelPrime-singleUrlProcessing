/*
package com.belprime.testTask;

import com.belprime.testTask.logic.PageExtractor;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

public class AppRunner2 {
    PageExtractor pageExtractor = new PageExtractor("belprime");

    public static void main(String[] args) {
        //The numbers are just silly tune parameters. Refer to the API.
        //The important thing is, we are passing a bounded queue.
        ExecutorService consumers = new ThreadPoolExecutor(
                1, 4, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10));
//        ExecutorService consumers = Executors.newFixedThreadPool(4);
        //No need to bound the queue for this executor.
        //Use utility method instead of the complicated Constructor.
        ExecutorService producers = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Runnable produce = new Produce(consumers);
        producers.submit(produce);
        producers.shutdown();
    }
}

class Produce implements Runnable {

    private final ExecutorService consumer;

    Produce(ExecutorService consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        Elements elements = null;
        try {
            elements = new PageExtractor("belprime").getSearchList();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Pancake cake = Pan.cook();
        Runnable consume = new Consume(elements);
        consumer.submit(consume);
    }
}

class Consume implements Runnable {
    //    private final Pancake cake;
    private final Elements elements;

    public Consume(Elements elements) {
        this.elements = elements;
    }

    @Override
    public void run() {
//        cake.eat();
        PageExtractor pe = new PageExtractor("belprime");
        try {
            Map<String, String> m = pe.getItems(elements);
            pe.displayItems(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
