package com.supremind.map;


import com.supremind.map.file.FileInfo;
import com.supremind.map.file.Files;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

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
        //        FileInfo fileInfo = Files.read("E:\\JDK\\jdk8\\src\\java\\util\\concurrent",
//                new String[]{"java"});
        FileInfo fileInfo = Files.read("E:\\JDK\\jdk8\\src\\java\\util\\concurrent",
                new String[]{"java"});
        System.out.println("文件数量" + fileInfo.getFiles());
        System.out.println("代码行数" + fileInfo.getLines());
        String[] words = fileInfo.words();
        System.out.println("单词数量" + words.length);
        Map<String,Integer> map = new TreeMap<>();
        for (int i = 0; i < words.length; i++) {
            Integer count = map.get(words[i]);
            count = (count == null) ? 1 : (count + 1);
            map.put(words[i],count);
        }
        System.out.println(map.size());
    }
}
