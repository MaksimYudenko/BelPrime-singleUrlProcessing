package com.belprime.testTask.logic;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.belprime.testTask.util.Constants.BQ_CAPACITY;
import static com.belprime.testTask.util.Constants.POLL_TIMEOUT;

public class WebSearchService implements Runnable {

    private String[] messages;

    public WebSearchService(String[] messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        BlockingQueue<Elements> queue = new ArrayBlockingQueue<>(BQ_CAPACITY);
        Map<String, String> map = new ConcurrentHashMap<>();
        for (String message : messages) {
            Producer producer = new Producer(queue, message);
            Consumer consumer = new Consumer(queue, map);
            new Thread(producer).start();
            new Thread(consumer).start();
        }
    }
}

class Producer implements Runnable {

    private BlockingQueue<Elements> queue;
    private String message;

    Producer(BlockingQueue<Elements> queue, String message) {
        this.queue = queue;
        this.message = message;
    }

    @Override
    public void run() {
        PageExtractor pe = new PageExtractor(message);
        try {
            queue.put(pe.getSearchList());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Consumer implements Runnable {

    private BlockingQueue<Elements> queue;
    private Map<String, String> map;

    Consumer(BlockingQueue<Elements> queue, Map<String, String> map) {
        this.queue = queue;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            Elements elements = queue.poll(POLL_TIMEOUT, TimeUnit.SECONDS);
            assert elements != null;
            for (Element link : elements) {
                final String url = PageExtractor.getUrl(link);
                if (url.isEmpty()) continue;
                final String title = PageExtractor.getTitle(url);
                if (title.isEmpty()) continue;
                System.out.printf("URL %s \tTITLE %s\n", url, title);
                map.put(url, title);
            }
        } catch (NullPointerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}