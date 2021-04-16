package com.supremind.map;

import static org.junit.Assert.assertTrue;

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
     Map<String,Integer> map = new TreeMap<>();
     map.put("c",2);
     map.put("a",5);
     map.put("b",6);
     map.put("a",8);
     map.traversal(new Map.Visitor<String, Integer>() {
         @Override
         public boolean visit(String key, Integer value) {
             System.out.println(key + "_" +value);
             return false;
         }
     });
    }
    @Test
    public void test2() throws Exception{

    }
}
