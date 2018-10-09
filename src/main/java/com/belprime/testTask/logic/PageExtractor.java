package com.belprime.testTask.logic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.belprime.testTask.util.Constants.*;

public final class PageExtractor {

    private final String request;

    public PageExtractor(String request) {
        this.request = request;
    }

    Elements getSearchList() throws IOException {
        String preparedUrl = SEARCHING_MACHINE + URLEncoder.encode(request, CHARSET);
        final Document document = Jsoup.connect(preparedUrl +
                "&start=0&num=" + (LINKS_QUANTITY + 5)).userAgent(USER_AGENT)
                .ignoreHttpErrors(true).validateTLSCertificates(false).get();
//                                 LINKS_QUANTITY + 5 - for guaranteed map filling
        return document.select(".g>.r>a");
    }

    static String getUrl(Element link) {
        String url = link.absUrl("href");
        try {
            url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), CHARSET);
            if (!url.startsWith(PROTOCOL_NAME))
                url = "";
           /* if (url.contains(IGNORED_SITE)) {
                System.err.println("http error: status 999 > ignored URL " + url);
                url = "";
            }*/
        } catch (UnsupportedEncodingException e) {
            System.err.println("getUrl() error:");
            e.printStackTrace();
        }
        return url;
    }


    static String getTitle(String url) {
        String title = "";
        try {
            title = Jsoup.connect(url).userAgent(USER_AGENT)
                    .ignoreHttpErrors(true).validateTLSCertificates(false).get().title();
        } catch (IOException e) {
            System.err.println("getTitle() error:");
            e.printStackTrace();
        }
        return title;
    }

    private static Map<String, String> getItems(Elements links) {
        final Map<String, String> map = new LinkedHashMap<>();
        for (Element link : links) {
            final String url = getUrl(link);
            if (url.isEmpty()) continue;
            final String title = getTitle(url);
//          if we don't require to store an empty titles >
            if (title.isEmpty()) continue;
            map.put(url, title);
            if (map.size() == LINKS_QUANTITY) break;
        }
        return map;
    }

    public void displayItems() {
        Map<String, String> map = null;
        try {
            map = getItems(getSearchList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert map != null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.printf("URL %s \tTITLE %s\n", entry.getKey(), entry.getValue());
        }
    }

}