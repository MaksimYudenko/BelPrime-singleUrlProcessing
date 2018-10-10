package com.belprime.testTaskSingleUrlProcessing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.belprime.testTaskSingleUrlProcessing.util.Constants.REGEX;
import static com.belprime.testTaskSingleUrlProcessing.util.Constants.WELCOME_PHRASE;

public class MessageProvider {

    public static String[] getUserRequests() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(WELCOME_PHRASE);
        String msgLine = null;
        try {
            msgLine = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert msgLine != null;
        return msgLine.split(REGEX);
    }

}