//package com.belprime.testTask;
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.LinkedBlockingQueue;
//
//public class Main3 {
//    public static void main(String args[]) {
//        BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<Integer>();
//
//        ExecutorService pes = Executors.newFixedThreadPool(2);
//        ExecutorService ces = Executors.newFixedThreadPool(2);
//
//        pes.submit(new Producer(sharedQueue, 1));
//        pes.submit(new Producer(sharedQueue, 2));
//        ces.submit(new Consumer(sharedQueue, 1));
//        ces.submit(new Consumer(sharedQueue, 2));
//        // shutdown should happen somewhere along with awaitTermination
//         // *https:
////stackoverflow.com/questions/36644043/how-to-properly-shutdown-java-executorservice/36644320#36644320 */
//        pes.shutdown();
//        ces.shutdown();
//    }
//}
//
//class Producer implements Runnable {
//    private final BlockingQueue<Integer> sharedQueue;
//    private int threadNo;
//
//    public Producer(BlockingQueue<Integer> sharedQueue, int threadNo) {
//        this.threadNo = threadNo;
//        this.sharedQueue = sharedQueue;
//    }
//
//    @Override
//    public void run() {
//        for (int i = 1; i <= 3; i++) {
//            try {
//                //получаю список URL
//                int number = i + (10 * threadNo);
//                System.out.println("Produced:" + number + ":by thread:" + threadNo);
//                //кладу список в sharedQueue
//                sharedQueue.put(number);
//            } catch (Exception err) {
//                err.printStackTrace();
//            }
//        }
//    }
//}
//
//class Consumer implements Runnable {
//    private final BlockingQueue<Integer> sharedQueue;
//    private int threadNo;
//
//    public Consumer(BlockingQueue<Integer> sharedQueue, int threadNo) {
//        this.sharedQueue = sharedQueue;
//        this.threadNo = threadNo;
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                //беру из Queue список
//                int num = sharedQueue.take();
//                //зазбираю список на элементы
//                System.out.println("Consumed: " + num + ":by thread:" + threadNo);
//            } catch (Exception err) {
//                err.printStackTrace();
//            }
//        }
//    }
//}