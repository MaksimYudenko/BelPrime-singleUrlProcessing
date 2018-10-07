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

import static com.belprime.testTask.util.Constants.*;

public final class PageExtractor {

    private final String request;

    public PageExtractor(String request) {
        this.request = request;
    }

    private Map<String, String> getElements() {
        final Map<String, String> map = new LinkedHashMap<>();
        try {
            String preparedUrl = SEARCHING_MACHINE + URLEncoder.encode(request, CHARSET);
            final Document document = Jsoup.connect(preparedUrl +
                    "&start=0&num=" + (LINKS_QUANTITY * 2)).userAgent(USER_AGENT).get();
            Elements links = document.select(".g>.r>a");
            for (Element link : links) {
                if (map.size() == LINKS_QUANTITY) break;
                String url = link.absUrl("href");
                url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), CHARSET);
                if (!url.startsWith(PROTOCOL_NAME)) {
                    continue;
                }
//url.contains(IGNORED_SITE) below - check url to avoid the "http error fetching URL. Status=999",
// which "LinkedIn.com"-like site generates because of User-Agent and proxy.
                if (url.contains(IGNORED_SITE)) {
                    System.err.println("http error status=999: URL " + url + " ignored.");
                    continue;
                }
                final String title = Jsoup.connect(url).get().title();
                if (title.isEmpty()) continue;//if we don't need empty titles
                map.put(url, title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public void printMap() {
        Map<String, String> map = getElements();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("URL " + entry.getKey() + " TITLE " + entry.getValue());
        }
    }

}