package com.belprime.testTask;

import com.belprime.testTask.logic.WebSearchService;
import com.belprime.testTask.util.MessageProvider;

public class Main {

    public static void main(String[] args) {
        new Thread(new WebSearchService(MessageProvider.getMessages())).start();
    }

}