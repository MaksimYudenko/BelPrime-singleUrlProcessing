package com.belprime.testTask.logic;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.belprime.testTask.util.Constants.*;

public final class PageExtractor {

    private final String request;
    private static final Logger logger = Logger.getLogger(PageExtractor.class.getName());

    public PageExtractor(String request) {
        this.request = request;
    }

    public List<Element> getSearchList() {
        List<Element> elementList = new ArrayList<>();
        try {
            String preparedUrl = SEARCHING_MACHINE + URLEncoder.encode(request, CHARSET);
            final Document document = Jsoup.connect(preparedUrl +
                    "&start=0&num=" + (LINKS_QUANTITY + 5)).userAgent(USER_AGENT)
                    .ignoreHttpErrors(true).validateTLSCertificates(false).get();
//                                 LINKS_QUANTITY + 5 - for guaranteed map filling
            elementList.addAll(document.select(".g>.r>a"));
        } catch (IOException e) {
            logger.log(Level.ALL, "getSearchList() throws IOE " + e.getMessage());
        }
        return elementList;
    }

    public static String getUrl(Element link) {
        String url = "";
        String linkAbs = link.absUrl("href");
        try {
            assert linkAbs != null;
            url = getVerifiedUrl(URLDecoder.decode(
                    linkAbs.substring(linkAbs.indexOf('=') + 1, linkAbs.indexOf('&')), CHARSET));
        } catch (NullPointerException | UnsupportedEncodingException e) {
            logger.log(Level.ALL, "Exception in getUrl():", e);
        }
        return url;
    }

    @NotNull
    private static String getVerifiedUrl(String url) {
        if (!url.startsWith(PROTOCOL_NAME)) {
            logger.log(Level.ALL, "URL <" + url + "> doesn't start with "
                    + PROTOCOL_NAME + " and will be ignored.");
            url = "";
        }
        if (url.contains(IGNORED_SITE)) {
            logger.log(Level.ALL, "URL <" + url + "> throws http error: status 999 and will be ignored.");
            url = "";
        }
        return url;
    }

    public static String getTitle(String url) {
        String title = "";
        try {
            title = Jsoup.connect(url).userAgent(USER_AGENT)
                    .ignoreHttpErrors(true).validateTLSCertificates(false).get().title();
            if (title.isEmpty()) {
                logger.log(Level.ALL, "URL <" + url + "> has an empty <title> and will be ignored.");
                return "";
            }
        } catch (IOException e) {
            logger.log(Level.ALL, "Exception in getTitle():", e);
        }
        return title;
    }

    public static void displayItems(ConcurrentHashMap<String, String> map) {
        map.forEach((k, v) -> System.out.printf("URL %s \tTITLE %s\n", k, v));
    }

}