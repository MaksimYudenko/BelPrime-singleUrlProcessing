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

    Elements getSearchList() throws IOException {
        String preparedUrl = SEARCHING_MACHINE + URLEncoder.encode(request, CHARSET);
        final Document document = Jsoup.connect(preparedUrl +
                "&start=0&num=" + (LINKS_QUANTITY + 5)).userAgent(USER_AGENT)
                .ignoreHttpErrors(true).get();
//                                 LINKS_QUANTITY + 5 - for guaranteed map filling
        return document.select(".g>.r>a");
    }

    private static Map<String, String> getItems(Elements links) throws IOException {
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
            final String title = Jsoup.connect(url).userAgent(USER_AGENT)
                    .ignoreHttpErrors(true).get().title();
//          if we don't require to store an empty titles >
            if (title.isEmpty()) continue;
            map.put(url, title);
            if (map.size() == LINKS_QUANTITY) break;
        }
        return map;
    }

    static String getUrl(Element link) throws UnsupportedEncodingException {
        String url = link.absUrl("href");
        url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), CHARSET);
        if (!url.startsWith(PROTOCOL_NAME) & url.contains(IGNORED_SITE)) {
            url = "";
        }
        return url;
    }

    public void displayItems() {
        Map<String, String> map = extractElements();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.printf("URL %s \tTITLE %s\n", entry.getKey(), entry.getValue());
        }
    }

}