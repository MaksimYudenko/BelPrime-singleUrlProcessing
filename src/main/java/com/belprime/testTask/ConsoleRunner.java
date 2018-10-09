package com.belprime.testTask;

import com.belprime.testTask.logic.PageExtractor;
import com.belprime.testTask.util.MessageProvider;

public class ConsoleRunner {

    public static void main(String[] args) {
        for (String message : MessageProvider.getMessages())
            new PageExtractor(message).displayItems();
    }

}