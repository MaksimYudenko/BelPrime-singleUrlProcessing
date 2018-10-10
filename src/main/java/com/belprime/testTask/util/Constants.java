package com.belprime.testTaskSingleUrlProcessing.util;

public class Constants {

    public static final String SEARCHING_MACHINE = "HTTP://www.google.com/search?q=";
    public static final String CHARSET = "UTF-8";
    public static final String PROTOCOL_NAME = "http";
    public static final String USER_AGENT = "Mozilla/5.0(compatible;MSIE 10.0;Windows NT 6.2; SV1)";
    //    public static final String USER_AGENT = "ExampleBot 1.0 (+HTTP://belprime.com/bot)";
    public static final String IGNORED_SITE = "linkedin";
    public static final String LOGGER_PROPS = "./src/main/resources/log4j.properties";
    public static final Integer LINKS_QUANTITY = 10;
    public static final Integer BQ_CAPACITY = LINKS_QUANTITY * 10;
    public static final Integer POLL_TIMEOUT = (LINKS_QUANTITY / 2) < 40 ? 20 : LINKS_QUANTITY / 2;
    public static final Integer AWAIT_TERMINATION = (LINKS_QUANTITY / 3) < 40 ? 30 : LINKS_QUANTITY / 3;
    static final String WELCOME_PHRASE = "Type a phrase to search (as many as you'd like):";
    static final String REGEX = "\\s*(\\s{2,}|,|!|\\?|;|\\/|\\.)\\s*";
/*  search line for testing:
BelPrime! google;  лучший фильм 2018 года/      Nike или Adidas? Погода - Минск.
*/

}