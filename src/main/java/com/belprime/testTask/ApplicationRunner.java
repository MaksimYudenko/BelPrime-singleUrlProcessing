package com.belprime.testTask;

import com.belprime.testTask.logic.PageExtractor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.belprime.testTask.util.Constants.IGNORED_SITE;
import static com.belprime.testTask.util.Constants.PROTOCOL_NAME;

public class ApplicationRunner {
//    PageExtractor pageExtractor = new PageExtractor("belprime");

    public static void main(String[] args) {
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
       /* Scanner sc = new Scanner(System.in);
        String message = sc.next();*/
        String message = "belprime";
        BlockingQueue<Elements> queue = new ArrayBlockingQueue<>(10);
        Map<String, String> map = new ConcurrentHashMap<>();
        Producer p = new Producer(queue, message);
//        Producer p1 = new Producer(queue, "google");
        Consumer c = new Consumer(queue, map);
//        Consumer c1 = new Consumer(queue, map);

        new Thread(p).start();
//        new Thread(p1).start();
        new Thread(c).start();
//        new Thread(c1).start();
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
//            Thread.sleep(100);
//            queue.wait();
            queue.put(pe.getSearchList());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
//        queue.notifyAll();
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
            Elements elements = queue.poll(1000, TimeUnit.MILLISECONDS);
            assert elements != null;
            for (Element link : elements) {
                String url = PageExtractor.getUrl(link);
                if (!url.startsWith(PROTOCOL_NAME)) continue;
                if (url.contains(IGNORED_SITE)) {
                    System.err.println("http error: status 999 > ignored URL " + url);
                    continue;
                }

                final String title = Jsoup.connect(url).get().title();
                if (title.isEmpty()) continue;
                System.out.printf("URL %s \tTITLE %s\n", url, title);
                map.put(url, title);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}