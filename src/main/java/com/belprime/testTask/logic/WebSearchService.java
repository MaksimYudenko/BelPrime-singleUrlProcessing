package com.belprime.testTask.logic;

import org.apache.log4j.PropertyConfigurator;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.*;

import static com.belprime.testTask.util.Constants.*;

public class WebSearchService implements Runnable {

    private String[] messages;
    static volatile boolean isRunning = true;

    public WebSearchService(String[] messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        BlockingQueue<Elements> queue = new LinkedBlockingQueue<>(BQ_CAPACITY);
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        Producer producer = new Producer(queue, messages);
        Consumer consumer = new Consumer(queue, map);
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        PropertyConfigurator.configure(LOGGER_PROPS);

        new Thread(producer).start();
//
        for (int i = 0; i < messages.length; i++)
            executorService.execute(consumer);
        while (true) {
            if (!isRunning) break;
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(AWAIT_TERMINATION, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PageExtractor.displayItems(map);
    }
}

class Producer implements Runnable {

    private BlockingQueue<Elements> queue;
    private String[] messages;

    Producer(BlockingQueue<Elements> queue, String[] messages) {
        this.queue = queue;
        this.messages = messages;
    }

    @Override
    public void run() {
        try {
            for (String message : messages)
                queue.put(new PageExtractor(message).getSearchList());
            queue.put(new Elements());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Consumer implements Runnable {

    private BlockingQueue<Elements> queue;
    private ConcurrentHashMap<String, String> map;

    Consumer(BlockingQueue<Elements> queue, ConcurrentHashMap<String, String> map) {
        this.queue = queue;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            while (WebSearchService.isRunning) {
                Elements elements = queue.poll(POLL_TIMEOUT, TimeUnit.SECONDS);
                assert elements != null;
                final ConcurrentHashMap<String, String> items = PageExtractor.getItems(elements);
                if (elements.equals(new Elements()))
                    WebSearchService.isRunning = false;
                map.putAll(items);
            }
        } catch (NullPointerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}