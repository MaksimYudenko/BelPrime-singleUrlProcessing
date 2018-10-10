package com.belprime.testTask.logic;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.concurrent.*;

import static com.belprime.testTask.util.Constants.*;

public class WebSearchService implements Runnable {

    private String[] messages;
    static volatile boolean isRunning = true;
    private ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(WebSearchService.class.getName());

    public WebSearchService(String[] messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(BQ_CAPACITY);
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
            logger.log(Level.ALL, "WebSearchService throws exception:");
            e.printStackTrace();
        }
        PageExtractor.displayItems(map);
    }
}

class Producer implements Runnable {

    private BlockingQueue<String> queue;
    private String[] messages;
    private static final Logger logger = Logger.getLogger(Producer.class.getName());

    Producer(BlockingQueue<String> queue, String[] messages) {
        this.queue = queue;
        this.messages = messages;
    }

    @Override
    public void run() {
        try {
            for (String message : messages) {
                final List<Element> searchList = new PageExtractor(message).getSearchList();
                for (Element link : searchList) {
                    String url = PageExtractor.getUrl(link);
                    assert url != null;
                    if (url.isEmpty()) continue;
                    queue.put(url);
                }
            }
            queue.put("done");
        } catch (InterruptedException e) {
            logger.log(Level.ALL, "Producer throws exception:");
            e.printStackTrace();
        }
    }

}

class Consumer implements Runnable {

    private BlockingQueue<String> queue;
    private ConcurrentHashMap<String, String> map;
    private static final Logger logger = Logger.getLogger(Consumer.class.getName());

    Consumer(BlockingQueue<String> queue, ConcurrentHashMap<String, String> map) {
        this.queue = queue;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            while (WebSearchService.isRunning) {
                String url = queue.poll(POLL_TIMEOUT, TimeUnit.SECONDS);
                assert url != null;
                if (url.equalsIgnoreCase("done")) {
                    WebSearchService.isRunning = false;
                    continue;
                }
                if (url.isEmpty()) continue;
                if (map.size() == LINKS_QUANTITY) break;
                map.put(url, PageExtractor.getTitle(url));
            }
        } catch (NullPointerException | InterruptedException e) {
            logger.log(Level.ALL, "Consumer throws exception:");
            e.printStackTrace();
        }
    }

}