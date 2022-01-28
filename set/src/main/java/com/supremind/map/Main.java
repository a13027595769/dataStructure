package com.supremind.map;


import com.supremind.map.file.FileInfo;
import com.supremind.map.file.Files;
import com.supremind.map.set.TreeSet;
import com.supremind.map.set.ListSet;
import com.supremind.map.set.Set;

import java.util.Collection;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        test2();
    }
    static void testSet(Set<String> set,String[] words){
        for (int i = 0; i < words.length; i++) {
            set.add(words[i]);
        }
        for (int i = 0; i < words.length; i++) {
            set.contains(words[i]);
        }
        for (int i = 0; i < words.length; i++) {
            set.remove(words[i]);
        }
    }
    private static void test2(){
//        FileInfo fileInfo = Files.read("E:\\JDK\\jdk8\\src\\java\\util\\concurrent",
//                new String[]{"java"});
        FileInfo fileInfo = Files.read("E:\\JDK\\jdk8\\src\\java",
                new String[]{"java"});
        System.out.println("文件数量" + fileInfo.getFiles());
        System.out.println("代码行数" + fileInfo.getLines());
        String[] words = fileInfo.words();
        System.out.println("单词数量" + words.length);
//        Times.test("ListSet", () -> {
//            testSet(new ListSet<>(),words);
//        });
        Times.test("TreeSet", () -> {
            testSet(new TreeSet<>(),words);
        });
    }

    private static void test1() {
        Set<Integer> listSet  = new ListSet<>();
        listSet.add(12);
        listSet.add(10);
        listSet.add(11);
        listSet.add(11);

        Set<Integer> treeSet  = new TreeSet<>();
        treeSet.add(12);
        treeSet.add(10);
        treeSet.add(11);
        treeSet.add(11);

        treeSet.add(10);
        treeSet.add(7);
        treeSet.add(9);
        treeSet.traversal(new Set.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.println(element);
                return false;
            }
        });
    }
}
