package com.belprime.testTask;

import com.belprime.testTask.logic.PageExtractor;
import org.jsoup.select.Elements;

import java.util.concurrent.*;

public class AppRunner2 {
    public static void main(String[] args) {
        //The numbers are just silly tune parameters. Refer to the API.
        //The important thing is, we are passing a bounded queue.
        ExecutorService consumer = new ThreadPoolExecutor(1, 4, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100));

        //No need to bound the queue for this executor.
        //Use utility method instead of the complicated Constructor.
        ExecutorService producer = Executors.newSingleThreadExecutor();

        Runnable produce = new Produce(consumer);
        producer.submit(produce);
    }
}

class Produce implements Runnable {
    private final ExecutorService consumer;

    public Produce(ExecutorService consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        Elements elements = new PageExtractor("belprime").getSearchList();
//        Pancake cake = Pan.cook();
        Runnable consume = new Consume(elements);
        consumer.submit(consume);
    }
}

class Consume implements Runnable {
    private final Pancake cake;

    public Consume(Pancake cake) {
        this.cake = cake;
    }

    @Override
    public void run() {
        cake.eat();
    }
}