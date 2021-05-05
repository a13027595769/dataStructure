package com.supremind;

import static org.junit.Assert.assertTrue;

import com.supremind.heap.BinaryHeap;
import com.supremind.map.asserta.printer.BinaryTrees;
import com.supremind.queue.PriorityQueue;
import org.junit.Test;

import java.util.Comparator;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void test1() throws Exception {
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.add(68);
        heap.add(72);
        heap.add(43);
        heap.add(50);
        heap.add(38);
        heap.add(10);
        heap.add(90);
        heap.add(65);
        BinaryTrees.println(heap);
//        heap.remove();
//        BinaryTrees.println(heap);
        System.out.println(heap.replace(70));
        BinaryTrees.println(heap);

    }

    @Test
    public void test2() throws Exception {
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        BinaryHeap<Integer> heap = new BinaryHeap<>(data);
        BinaryTrees.println(heap);
        data[0] = 10;
        data[0] = 20;
        BinaryTrees.println(heap);

    }

    @Test
    public void test3() throws Exception {
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
//        BinaryHeap<Integer> heap1 = new BinaryHeap<>(data, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o1 - o2;
//            }
//        });
        BinaryHeap<Integer> heap2 = new BinaryHeap<>(data, (o1, o2) -> o2 - o1);

        BinaryTrees.println(heap2);

    }
    @Test
    public void test4() throws Exception {
        //TOP K问题必须要用小顶堆，因为你肯定是要全部扫描的，如果大顶堆，你到后面，还是得上滤，小顶堆添加的时候
        //可能最大的就是在最后面，扫描完了，直接从最后面取就好了。
        BinaryHeap<Integer> heap = new BinaryHeap<>( (o1, o2) -> o2 - o1);
        int k = 4;
        Integer[] data = {51, 30, 39, 92, 74, 25, 16, 93,
                91, 19, 54, 47, 73, 62, 76, 63, 35, 18,
                90, 6, 65, 49, 3, 26, 61, 21, 48};
        for (Integer datum : data) {
            if (heap.size() < k) {
                heap.add(datum);
                System.out.println("add-------------------------" + datum);
                BinaryTrees.println(heap);

            } else if (datum > heap.get()) {
                heap.replace(datum);
                System.out.println("replace---------------------" + datum);
                BinaryTrees.println(heap);

            }
        }

        BinaryTrees.println(heap);

    }
    @Test
    public void test5() throws Exception {
        PriorityQueue<Person> queue = new PriorityQueue<>();
        queue.enQueue(new Person("Jack",2));
        queue.enQueue(new Person("Rose",10));
        queue.enQueue(new Person("Jake",5));
        queue.enQueue(new Person("James",15));
        while (!queue.isEmpty()){
            System.out.println(queue.deQueue());
        }
        //java.util.PriorityQueue
    }

}
