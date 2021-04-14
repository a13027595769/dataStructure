package com.supremind.Queue;

public class Main {
    static void test1(){

        Queue<Integer> queue = new Queue<>();
        queue.enQueue(11);
        queue.enQueue(22);
        queue.enQueue(33);
        queue.enQueue(44);
        while (!queue.isEmpty()){
            System.out.println(queue.deQueue());
            System.out.println(queue);
        }
    }
    static void test2(){
        CircleQueue<Integer> queue = new CircleQueue<>();
        // 0 1 2 3 4 5 6 7 8 9
        for (int i = 0; i < 10; i++) {
            queue.enQueue(i);
        }
        // null null null null null 5 6 7 8 9
        for (int i = 0; i < 5; i++) {
            queue.deQueue();
        }
        // null null null null null 5 6 7 8 9
        for (int i = 15; i < 20; i++) {
            queue.enQueue(i);
        }
        System.out.println(queue);
        while (!queue.isEmpty()){
            System.out.println(queue.deQueue());
        }
    }
    static void test3(){
        CircleDeque<Integer> queue = new CircleDeque<>();
        // 头 8, 7, 6, 5, 4, 3, 2, 1, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, null, null, 10, 9 尾部
        for (int i = 0; i < 10; i++) {
            queue.enQueueFront(i + 1);
            queue.enQueueRear(i + 100);
        }
        for (int i = 0; i < 3; i++) {
            queue.deQueueFront();
            queue.deQueueRear();
        }
        queue.enQueueFront(11);
        queue.enQueueFront(22);
        System.out.println(queue);
        while (!queue.isEmpty()){
            System.out.println(queue.deQueueFront());
        }
    }

    public static void main(String[] args) {
        test2();
        test3();
    }
}
