/*
package com.belprime.testTask;

import com.belprime.testTask.logic.PageExtractor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

public class Main3 {
    public static void main(String args[]) {
        String request1 = "belprime";
        String request2 = "google";

        BlockingQueue<Elements> sharedQueue = new LinkedBlockingQueue<Elements>();

        Map<String, String> map = new ConcurrentHashMap<>();

        ExecutorService pes = Executors.newFixedThreadPool(2);
        ExecutorService ces = Executors.newFixedThreadPool(4);

        pes.submit(new Producer(sharedQueue, request1));
        pes.submit(new Producer(sharedQueue, request2));
        ces.submit(new Consumer(sharedQueue, request1));
        ces.submit(new Consumer(sharedQueue, request2));
//        ces.submit(new Consumer(sharedQueue, request2));

        pes.shutdown();
        ces.shutdown();

    }
}

class Producer implements Runnable {
    private final BlockingQueue<Elements> sharedQueue;
    private String request;

    public Producer(BlockingQueue<Elements> sharedQueue, String request) {
        this.sharedQueue = sharedQueue;
        this.request = request;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 2; i++) {
            try {
                //получаю список URL
                Elements elements = new PageExtractor(request).getSearchList();
                Thread.sleep(1000);

                System.out.println("Produced - getSearchList(): " + elements.size());
                //кладу список в sharedQueue
                sharedQueue.put(elements);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable {
    private final BlockingQueue<Elements> sharedQueue;
    private volatile boolean isRunning = true;
    private String request;

    public Consumer(BlockingQueue<Elements> sharedQueue, String request) {
//    public Consumer(BlockingQueue<Elements> sharedQueue, String request, Map<String, String>) {
        this.sharedQueue = sharedQueue;
        this.request = request;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Elements elements = sharedQueue.poll(500, TimeUnit.MILLISECONDS);
                //разбираю список на элементы
                Thread.sleep(500);
                assert elements != null;
                //беру из Queue список
                for (Element link : elements) {
                    System.out.println(PageExtractor.getUrl(link));
                }
                System.out.println("Consumed - getUrl() done.");
            } catch (InterruptedException | IOException e) {
                if (!isRunning && sharedQueue.isEmpty())
                    return;
            }
        }
    }
}
*/
