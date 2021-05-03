package com.supremind;

import static org.junit.Assert.assertTrue;

import com.supremind.heap.BinaryHeap;
import com.supremind.map.asserta.printer.BinaryTrees;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    @Test
    public void test1() throws Exception{
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.add(68);
        heap.add(72);
        heap.add(43);
        heap.add(50);
        heap.add(38);
        BinaryTrees.println(heap);
    }
}
