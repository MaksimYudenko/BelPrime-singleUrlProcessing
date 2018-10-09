package com.belprime.testTask;

import com.belprime.testTask.logic.WebSearchService2;
import com.belprime.testTask.util.MessageProvider;

public class Main {

    public static void main(String[] args) {
//        new Thread(new WebSearchService(MessageProvider.getMessages())).start();
        new Thread(new WebSearchService2(MessageProvider.getMessages())).start();
    }

}