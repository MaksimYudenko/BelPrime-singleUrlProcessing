package com.belprime.testTask;

import com.belprime.testTask.logic.PageExtractor;
import com.belprime.testTask.util.MessageProvider;

import java.io.IOException;

public class ConsoleRunner {

    public static void main(String[] args) {
        for (String message : MessageProvider.getUserRequests()) {
            try {
                PageExtractor.displayItems(PageExtractor.getItems(
                        new PageExtractor(message).getSearchList()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}