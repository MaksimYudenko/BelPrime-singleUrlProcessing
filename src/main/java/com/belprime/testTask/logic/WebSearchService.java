package com.belprime.testTask.logic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.belprime.testTask.util.Constants.*;

public class WebSearchService implements Runnable {

    private String[] messages;

//    public static void main(String[] args) {


        /*   ExecutorService consumers = new ThreadPoolExecutor(
        1, 4, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10));*/
//        ExecutorService consumers = Executors.newFixedThreadPool(4);
//        ExecutorService producers = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

/*
        Runnable produce = new Produce(consumers);
        producers.submit(produce);
        producers.shutdown();
*/

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
                String url = PageExtractor.getUrl(link);
                if (!url.startsWith(PROTOCOL_NAME)) continue;
            /*    if (url.contains(IGNORED_SITE)) {
                    System.err.println("http error: status 999 > ignored URL " + url);
                    continue;
                }*/

                final String title = Jsoup.connect(url).userAgent(USER_AGENT)
                        .ignoreHttpErrors(true).validateTLSCertificates(false).get().title();
                if (title.isEmpty()) continue;
                System.out.printf("URL %s \tTITLE %s\n", url, title);
                map.put(url, title);
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}