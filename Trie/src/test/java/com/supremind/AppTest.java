package com.supremind;

import static org.junit.Assert.assertTrue;

import com.supremind.map.asserta.Asserts;
import com.supremind.trie.Trie;
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
        Trie<Integer> trie = new Trie<>();
        trie.add("cat",1);
        trie.add("dog",2);
        trie.add("catalog",3);
        trie.add("cast",4);
        trie.add("小码哥",5);
        Asserts.test(trie.size() == 5);
//        System.out.println(trie.get("dog"));
//        System.out.println(trie.get("小码哥"));
        Asserts.test(trie.startsWith("ca"));
        Asserts.test(trie.startsWith("do"));
        Asserts.test(trie.startsWith("cata"));
        Asserts.test(trie.startsWith("cast"));
        Asserts.test(!trie.startsWith("hehe"));
        Asserts.test(trie.get("小码哥") == 5);
        Asserts.test(trie.remove("cat") == 1);
        Asserts.test(trie.remove("catalog") == 3);
        Asserts.test(trie.remove("cast") == 4);
        Asserts.test(trie.size() == 2);
        Asserts.test(trie.startsWith("do"));
        Asserts.test(trie.startsWith("小"));
        Asserts.test(!trie.startsWith("c"));
    }
}
