package com.belprime.testTaskSingleUrlProcessing;

import com.belprime.testTaskSingleUrlProcessing.logic.WebSearchService;
import com.belprime.testTaskSingleUrlProcessing.util.MessageProvider;

public class MainRunner {

    public static void main(String[] args) {
        new Thread(new WebSearchService(MessageProvider.getUserRequests())).start();
    }

}