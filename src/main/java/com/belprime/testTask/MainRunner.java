package com.belprime.testTask;

import com.belprime.testTask.logic.WebSearchService;
import com.belprime.testTask.util.MessageProvider;

public class MainRunner {

    public static void main(String[] args) {
        new Thread(new WebSearchService(MessageProvider.getUserRequests())).start();
    }

}