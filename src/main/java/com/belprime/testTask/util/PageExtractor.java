package com.belprime.testTask.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PageExtractor {

    private static final String SEARCHING_MACHINE = "HTTP://www.google.com/search?q=";
    private static final String CHARSET = "UTF-8";
    private static final String PROTOCOL_NAME = "http";
    private static final String USER_AGENT = "Mozilla/5.0(compatible;MSIE 10.0;Windows NT 6; SV1)";
    //    private static final String USER_AGENT = "ExampleBot 1.0 (+HTTP://belprime.com/bot)";
    private static final Integer LINKS_QUANTITY = 10;
    private final Map<String, String> map = new ConcurrentHashMap<>();
    private final String request;

    public PageExtractor(String request) {
        this.request = request;
    }

    private void getItems() {
        try {
            String preparedUrl = SEARCHING_MACHINE + URLEncoder.encode(request, CHARSET);
            final Document document = Jsoup.connect(preparedUrl +
                    "&start=0&num=" + (LINKS_QUANTITY + 3)).userAgent(USER_AGENT).get();
            Elements links = document.select(".g>.r>a");
            for (Element link : links) {
                if (map.size() == LINKS_QUANTITY) break;
                String url = link.absUrl("href");
                url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), CHARSET);
//url.contains("linkedin") - to check url to avoid the "http error fetching URL. Status=999",
// which throws LinkedIn.com because of User-Agent and proxy.
                if (!url.startsWith(PROTOCOL_NAME) | url.contains("linkedin")) {
                    continue;
                }
                final String title = Jsoup.connect(url).get().title();
                if (title.isEmpty()) continue;//if we don't need empty titles
                map.put(url, title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printMap() {
        getItems();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("URL " + entry.getKey() + " TITLE " + entry.getValue());
        }
    }

}