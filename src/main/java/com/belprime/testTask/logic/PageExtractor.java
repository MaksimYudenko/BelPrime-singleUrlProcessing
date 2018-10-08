package com.belprime.testTask.logic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.belprime.testTask.util.Constants.*;

public final class PageExtractor implements Runnable {
    private CountDownLatch latch;
    private final String request;


    public PageExtractor(String request) {
        this.request = request;
    }

    public PageExtractor(String request, CountDownLatch cdl) {
        this.request = request;
        latch = cdl;
        new Thread(this);
    }

    public void run() {
        try {
            for (int i = 0; i < CDL; i++) {
                System.out.println("получение элементов");
                extractElements();
                System.out.println("элементы получены");
                latch.countDown();
                System.out.println("latch.countDown() and sleep 100 ms");
                Thread.sleep(100);
                System.out.println("after sleep100, " + latch.toString());
                System.out.println("displayItems()");
                displayItems();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> extractElements() {
        Map<String, String> map = null;
        try {
            Elements links = getSearchList();
            map = getItems(links);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Elements getSearchList() throws IOException {
        String preparedUrl = SEARCHING_MACHINE + URLEncoder.encode(request, CHARSET);
        final Document document = Jsoup.connect(preparedUrl +
                "&start=0&num=" + (LINKS_QUANTITY * 2)).userAgent(USER_AGENT).get();
//                                 LINKS_QUANTITY * 2 - for guaranteed map filling
        return document.select(".g>.r>a");
    }

    public Map<String, String> getItems(Elements links) throws IOException {
        final Map<String, String> map = new LinkedHashMap<>();
        for (Element link : links) {
            String url = link.absUrl("href");
            url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), CHARSET);
            if (!url.startsWith(PROTOCOL_NAME)) continue;
//               url.contains(IGNORED_SITE) below - to avoid the "http error fetching URL. Status=999",
//               which LinkedIn-like site generates because of User-Agent and proxy.
            if (url.contains(IGNORED_SITE)) {
                System.err.println("http error: status 999 > ignored URL " + url);
                continue;
            }
            final String title = Jsoup.connect(url).get().title();
//          if we don't require to store an empty titles >
            if (title.isEmpty()) continue;
            map.put(url, title);
            if (map.size() == LINKS_QUANTITY) break;
        }
        return map;
    }

    public void displayItems() {
        Map<String, String> map = extractElements();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("URL\t" + entry.getKey() + "\tTITLE <" + entry.getValue() + ">");
        }
    }

}